package com.izzydrive.backend.service.payment.process;

import com.izzydrive.backend.dto.payment.CurrentPayingDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.payment.process.afterpayment.AfterPaymentService;
import com.izzydrive.backend.service.payment.process.transfer.PaymentTransferService;
import com.izzydrive.backend.service.payment.process.validation.PaymentValidationService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PaymentProcessServiceImpl implements PaymentProcessService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentProcessServiceImpl.class);

    private final PassengerService passengerService;

    private final DrivingService drivingService;

    private final AfterPaymentService afterPaymentService;

    private final PaymentValidationService paymentValidationService;

    private final PaymentTransferService paymentTransferService;

    @Override
    @Transactional
    public boolean approvePayment(CurrentPayingDTO currentPayingData) {
        Passenger passenger = passengerService.getCurrentlyLoggedPassenger();
        Driving driving = passenger.getCurrentDriving();
        if (driving == null || !driving.getDrivingState().equals(DrivingState.PAYMENT)) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_DO_NOT_HAVE_DRIVING_FOR_PAYMENT);
        }

        if (!paymentValidationService.validateForPaymentSessionExpiration(driving)) {
            throw new BadRequestException(ExceptionMessageConstants.PAYMENT_SESSION_EXPIRED);
        }

        if (paymentValidationService.validateForPassengerPaymentApproval(passenger, currentPayingData)) {
            saveCurrentPayingAndApprovalForPassenger(passenger, currentPayingData);
        }

        if (drivingService.allPassengersApprovedDriving(driving.getId()) && !driving.isLocked()) {
            return startPaymentProcess(driving);
        }
        return true;
    }

    private boolean startPaymentProcess(Driving driving) {
        try {
            LOG.info("Starting payment process");
            driving.setLocked(true);
            drivingService.saveAndFlush(driving);
            boolean result = paymentTransferService.payForAllPassengers(driving);
            if (result) {
                afterPaymentService.onSuccess(driving);
                return true;
            } else {
                afterPaymentService.onFailure(driving);
                return false;
            }
        } catch (OptimisticLockingFailureException ex) {
            LOG.info(String.format("concurrent read for driving service: %s", ex.getMessage()));
            throw new BadRequestException("Your request have already been processed");
        }
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
}
