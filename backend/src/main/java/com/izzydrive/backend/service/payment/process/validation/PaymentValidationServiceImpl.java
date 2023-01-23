package com.izzydrive.backend.service.payment.process.validation;

import com.izzydrive.backend.dto.payment.CurrentPayingDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.driving.rejection.DrivingRejectionService;
import com.izzydrive.backend.service.driving.validation.DrivingValidationService;
import com.izzydrive.backend.service.payment.data.PaymentDataService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentValidationServiceImpl implements PaymentValidationService {

    private final DrivingValidationService drivingValidationService;

    private final PassengerService passengerService;

    private final PaymentDataService paymentDataService;

    private final DrivingRejectionService drivingRejectionService;

    @Override
    public boolean validateForPaymentSessionExpiration(Driving driving) {
        if (driving.isReservation()) {
            if (drivingValidationService.reservationExpiredForPayment(driving)) {
                // TODO:: isti posao kao i iz joba, obrisati rezervaciju
                // i poslati notifikacije o istekloj sesiji za placanje
                return false;
            }
        } else {
            if (drivingValidationService.drivingExpiredForPayment(driving)) {
                drivingRejectionService.removeDrivingPaymentSessionExpired(driving.getId());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validateForPassengerPaymentApproval(Passenger passenger, CurrentPayingDTO currentPayingData) {
        if (passenger.isApprovedPaying()) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_ALREADY_APPROVED_THIS_PAYING);
        }
        if (passengerService.passengerDoesNotHavePayingData(passenger) && currentPayingData.isUsingExistingPayingInfo()) {
            throw new BadRequestException(ExceptionMessageConstants.CANT_USE_EXISTING_PAYING_DATA);
        }
        if (!currentPayingData.isUsingExistingPayingInfo()) {
            paymentDataService.validatePaymentKeyPair(currentPayingData.getOnceTimeKeyPair());
        }
        return true;
    }
}

