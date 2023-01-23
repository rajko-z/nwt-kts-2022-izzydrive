package com.izzydrive.backend.service.driving.rejection;

import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.users.driver.locker.DriverLockerService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DrivingRejectionServiceImpl implements DrivingRejectionService {

    private final PassengerService passengerService;

    private final NotificationService notificationService;

    private final DrivingService drivingService;

    private final DriverLockerService driverLockerService;

    @Override
    @Transactional
    public void rejectDrivingLinkedUser() {
        Passenger passenger = passengerService.getCurrentlyLoggedPassenger();
        Driving driving = passenger.getCurrentDriving();
        if (driving == null || !driving.getDrivingState().equals(DrivingState.PAYMENT)) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_DO_NOT_HAVE_DRIVING_FOR_PAYMENT);
        }
        List<String> passengersToSendNotifications = driving.getPassengers().stream()
                .map(Passenger::getEmail).collect(Collectors.toList());
        String startLocation = driving.getRoute().getStart().getName();
        String endLocation = driving.getRoute().getEnd().getName();

        rejectDriving(driving);

        notificationService.sendNotificationRejectDriving(passengersToSendNotifications, startLocation, endLocation);
    }

    @Override
    @Transactional
    public void removeDrivingPaymentSessionExpired(Long drivingId) {
        Driving driving = drivingService.getDrivingByIdWithDriverRouteAndPassengers(drivingId);

        List<String> passengersToSendNotifications = driving.getPassengers().stream()
                .map(Passenger::getEmail).collect(Collectors.toList());

        rejectDriving(driving);

        notificationService.sendNotificationForPaymentExpired(passengersToSendNotifications);
    }

    @Override
    @Transactional
    public void rejectDriving(Driving driving) {
        passengerService.deleteDrivingFromPassengers(driving.getPassengers());
        passengerService.resetPassengersPayingInfo(driving.getPassengers());
        driverLockerService.unlockDriver(driving.getDriver().getEmail());
        drivingService.delete(driving);
    }
}
