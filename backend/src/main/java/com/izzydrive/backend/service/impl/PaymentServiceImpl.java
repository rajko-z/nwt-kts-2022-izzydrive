package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.PaymentStatusDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.DrivingService;
import com.izzydrive.backend.service.PaymentService;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PassengerService passengerService;

    private final DrivingService drivingService;

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
