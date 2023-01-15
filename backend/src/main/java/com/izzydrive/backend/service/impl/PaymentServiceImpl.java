package com.izzydrive.backend.service.impl;

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
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

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
        boolean passengerApprovedToPay = this.drivingService.passengerApprovedToPayDriving(driving, passenger.getEmail());
        boolean waitingForOthers = waitingForOthersToApprovePayment(driving, passenger);
        boolean sessionExpired = drivingService.drivingExpiredForPayment(driving);

        return new PaymentStatusDTO(passengerApprovedToPay, waitingForOthers, sessionExpired);
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
        return new KeyPairDTO(passenger.getSecretKey(), passenger.getEthAddress());
    }

    @Override
    public void savePaymentDataForCurrentLoggedUser(KeyPairDTO paymentData) {
        if (!this.isValidSecretKey(paymentData.getSecretKey())) {
            throw new BadRequestException(ExceptionMessageConstants.ERROR_INVALID_SECRET_KEY);
        }
        if (!this.isValidETHAddress(paymentData.getEthAddress())) {
            throw new BadRequestException(ExceptionMessageConstants.ERROR_INVALID_ETH_ADDRESS);
        }
        if (!this.addressMatchSecretKey(paymentData)) {
            throw new BadRequestException(ExceptionMessageConstants.ERROR_ETH_ADDRESS_DOES_NOT_MATCH_SECRET_KEY);
        }
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
            if (!this.drivingService.passengerApprovedToPayDriving(driving, p.getEmail()))
            {
                return true;
            }
        }
        return false;
    }
}
