package com.izzydrive.backend.service.payment.data;

import com.izzydrive.backend.dto.payment.KeyPairDTO;
import com.izzydrive.backend.dto.payment.PaymentStatusDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.validation.DrivingValidationService;
import com.izzydrive.backend.service.payment.web3.Web3Service;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class PaymentDataServiceImpl implements PaymentDataService {

    private final PassengerService passengerService;

    private final DrivingValidationService drivingValidationService;

    private final Web3Service web3Service;

    @Override
    public PaymentStatusDTO getPaymentStatusForLoggedPassenger() {
        Passenger passenger = passengerService.getCurrentlyLoggedPassenger();
        Driving driving = passenger.getCurrentDriving();
        if (driving == null || !driving.getDrivingState().equals(DrivingState.PAYMENT)) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_DO_NOT_HAVE_DRIVING_FOR_PAYMENT);
        }
        BigDecimal priceInRSD = BigDecimal.valueOf(driving.getPrice() / driving.getPassengers().size());
        BigDecimal priceInEth = this.web3Service.convertRSDToEth(priceInRSD);
        boolean passengerApprovedToPay = passenger.isApprovedPaying();
        boolean waitingForOthers = waitingForOthersToApprovePayment(driving, passenger);
        boolean sessionExpired = drivingValidationService.drivingExpiredForPayment(driving);

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
    public void validatePaymentKeyPair(KeyPairDTO paymentData) {
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
