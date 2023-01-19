package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.payment.CurrentPayingDTO;
import com.izzydrive.backend.dto.payment.KeyPairDTO;
import com.izzydrive.backend.dto.payment.PaymentStatusDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.DrivingService;
import com.izzydrive.backend.service.PaymentService;
import com.izzydrive.backend.service.Web3Service;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PassengerService passengerService;

    private final DrivingService drivingService;

    private final Web3Service web3Service;

    @Override
    public PaymentStatusDTO getPaymentStatusForLoggedPassenger() {
        Passenger passenger = passengerService.getCurrentlyLoggedPassenger();
        Driving driving = passenger.getCurrentDriving();
        if (driving == null || !driving.getDrivingState().equals(DrivingState.PAYMENT)) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_DON_NOT_HAVING_DRIVING_FOR_PAYMENT);
        }
        BigDecimal priceInRSD = BigDecimal.valueOf(driving.getPrice() / driving.getPassengers().size());
        BigDecimal priceInEth = this.web3Service.convertRSDToEth(priceInRSD);
        boolean passengerApprovedToPay = passenger.isApprovedPaying();
        boolean waitingForOthers = waitingForOthersToApprovePayment(driving, passenger);
        boolean sessionExpired = drivingService.drivingExpiredForPayment(driving);

        return new PaymentStatusDTO(passengerApprovedToPay, waitingForOthers, sessionExpired, priceInRSD.doubleValue(), priceInEth.doubleValue());
    }

    @Override
    public boolean isValidETHAddress(String address) {
        return web3Service.isValidETHAddress(address);
    }

    @Override
    public boolean isValidSecretKey(String key) {
        return web3Service.isValidSecretKey(key);
    }

    @Override
    public boolean addressMatchSecretKey(KeyPairDTO keyPairDTO) {
        return web3Service.addressMatchSecretKey(keyPairDTO.getSecretKey(), keyPairDTO.getEthAddress());
    }

    @Override
    public KeyPairDTO getPaymentDataForCurrentLoggedUser() {
        Passenger passenger = this.passengerService.getCurrentlyLoggedPassenger();
        if (passengerService.passengerDoesNotHavePayingData(passenger)) {
            return null;
        }
        return new KeyPairDTO(passenger.getSecretKey(), passenger.getEthAddress());
    }

    @Override
    public void savePaymentDataForCurrentLoggedUser(KeyPairDTO paymentData) {
        validatePaymentKeyPair(paymentData);
        Passenger passenger = passengerService.getCurrentlyLoggedPassenger();
        passenger.setEthAddress(paymentData.getEthAddress());
        passenger.setSecretKey(paymentData.getSecretKey());
        passengerService.save(passenger);
    }

    private boolean waitingForOthersToApprovePayment(Driving driving, Passenger currPassenger) {
        if (driving.getPassengers().size() == 1) {
            return false;
        }
        for (Passenger p : driving.getPassengers()) {
            if (p.getEmail().equals(currPassenger.getEmail())) {
                continue;
            }
            if (!p.isApprovedPaying())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void pay(CurrentPayingDTO currentPayingData) {
        Passenger passenger = passengerService.getCurrentlyLoggedPassenger();
        Driving driving = passenger.getCurrentDriving();
        if (driving == null || !driving.getDrivingState().equals(DrivingState.PAYMENT)) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_DON_NOT_HAVING_DRIVING_FOR_PAYMENT);
        }

        if (drivingService.drivingExpiredForPayment(passenger.getCurrentDriving())) {
            this.drivingService.removeDrivingPaymentSessionExpired(passenger.getCurrentDriving().getId());
            return;
        }

        if (passenger.isApprovedPaying()) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_ALREADY_APPROVED_THIS_PAYING);
        }

        if (passengerService.passengerDoesNotHavePayingData(passenger) && currentPayingData.isUsingExistingPayingInfo()) {
            throw new BadRequestException(ExceptionMessageConstants.CANT_USE_EXISTING_PAYING_DATA);
        }

        if (!currentPayingData.isUsingExistingPayingInfo()) {
            validatePaymentKeyPair(currentPayingData.getOnceTimeKeyPair());
        }

        saveCurrentPayingAndApprovalForPassenger(passenger, currentPayingData);
        if (drivingService.allPassengersApproveDriving(driving.getId())
                && !driving.isLocked())
        {
            startPaymentProcess(driving);
        }
    }

    private void startPaymentProcess(Driving driving) {
        try {
            LOG.info("Starting payment process");
            driving.setLocked(true);
            drivingService.saveAndFlush(driving);

            boolean result = payForAllPassengers(driving);
            if (result) {
                drivingService.setUpDrivingAfterSuccessPaymentAndSendNotification(driving);
            } else {
                drivingService.cleanUpDrivingAfterFailurePaymentAndSendNotification(driving);
            }
            LOG.info("Finishing payment process");
        } catch (OptimisticLockingFailureException ex) {
            LOG.info("concurent read for driving service: " + ex.getMessage());
            throw new BadRequestException("Your request have already been proceseed");
        }
    }

    private BigDecimal getPriceInEthPassengerHasToPay(Driving driving) {
        BigDecimal priceInRSD = BigDecimal.valueOf(driving.getPrice() / driving.getPassengers().size());
        return this.web3Service.convertRSDToEth(priceInRSD);
    }

    private boolean everyPassengerHaveEnoughOnBalance(Driving driving) {
        Set<Passenger> passengers = driving.getPassengers();
        BigDecimal priceToPay = getPriceInEthPassengerHasToPay(driving);
        BigDecimal minimumPriceToHaveOnBalance = priceToPay.add(new BigDecimal("0.01"));

        for (Passenger p : passengers) {
            String passengerAddress = p.isPayingUsingExistingInfo() ?
                    p.getEthAddress() : p.getOnceTimeEthAddress();

            try {
                BigDecimal balance = web3Service.getBalanceFromEthAddress(passengerAddress);
                if (balance.compareTo(minimumPriceToHaveOnBalance) < 0) {
                    return false;
                }
            } catch (ExecutionException | TimeoutException e) {
                LOG.error("From getbalance:" + e.getMessage());
                return false;
            } catch (InterruptedException e) {
                LOG.error("From getbalance:" + e.getMessage());
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }

    private boolean payForAllPassengers(Driving driving) {
        if (!everyPassengerHaveEnoughOnBalance(driving)) {
            return false;
        }
        BigDecimal priceToPay = getPriceInEthPassengerHasToPay(driving);

        for (Passenger p : driving.getPassengers()) {
            String sk = p.isPayingUsingExistingInfo() ? p.getSecretKey() : p.getOnceTimeSecretKey();
            try {
                if (!web3Service.pay(sk, priceToPay)) {
                    return false;
                }
            } catch (ExecutionException | TimeoutException e) {
                LOG.error("From payment process timeout or execution: " + e.getMessage());
                return false;
            } catch (InterruptedException e) {
                LOG.error("From payment process interrupt: " + e.getMessage());
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }

    private void saveCurrentPayingAndApprovalForPassenger(Passenger passenger, CurrentPayingDTO currentPayingData) {
        passenger.setPayingUsingExistingInfo(currentPayingData.isUsingExistingPayingInfo());
        passenger.setApprovedPaying(true);

        if (!currentPayingData.isUsingExistingPayingInfo()) {
            passenger.setOnceTimeEthAddress(currentPayingData.getOnceTimeKeyPair().getEthAddress());
            passenger.setOnceTimeSecretKey(currentPayingData.getOnceTimeKeyPair().getSecretKey());
        }
        this.passengerService.saveAndFlush(passenger);
    }

    private void validatePaymentKeyPair(KeyPairDTO paymentData) {
        if (!this.isValidSecretKey(paymentData.getSecretKey())) {
            throw new BadRequestException(ExceptionMessageConstants.ERROR_INVALID_SECRET_KEY);
        }
        if (!this.isValidETHAddress(paymentData.getEthAddress())) {
            throw new BadRequestException(ExceptionMessageConstants.ERROR_INVALID_ETH_ADDRESS);
        }
        if (!this.addressMatchSecretKey(paymentData)) {
            throw new BadRequestException(ExceptionMessageConstants.ERROR_ETH_ADDRESS_DOES_NOT_MATCH_SECRET_KEY);
        }
    }
}
