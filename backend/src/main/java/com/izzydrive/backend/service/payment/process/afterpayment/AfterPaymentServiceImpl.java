package com.izzydrive.backend.service.payment.process.afterpayment;

import com.izzydrive.backend.converters.DrivingConverter;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.driving.rejection.DrivingRejectionService;
import com.izzydrive.backend.service.navigation.NavigationService;
import com.izzydrive.backend.service.notification.driver.DriverNotificationService;
import com.izzydrive.backend.service.users.driver.locker.DriverLockerService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AfterPaymentServiceImpl implements AfterPaymentService {

    private final PassengerService passengerService;

    private final NotificationService notificationService;

    private final DrivingService drivingService;

    private final NavigationService navigationService;

    private final DriverNotificationService driverNotificationService;

    private final DriverLockerService driverLockerService;

    private final DrivingRejectionService drivingRejectionService;

    @Override
    @Transactional
    public void onSuccess(Driving driving) {
        driving.setDrivingState(DrivingState.WAITING);
        driving.setLocked(false);

        changeDriverStatusAndStartNavigationSystem(driving);

        passengerService.resetPassengersPayingInfo(driving.getPassengers());
        driverLockerService.unlockDriver(driving.getDriver().getEmail());

        notificationService.sendNotificationForPaymentSuccess(driving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
        notificationService.sendNotificationNewDrivingDriver(driving.getDriver().getEmail());
    }

    private void changeDriverStatusAndStartNavigationSystem(Driving driving) {
        Driver driver = driving.getDriver();
        Driving drivingWithLocations = drivingService.getDrivingWithLocations(driving.getId());

        if (driver.getDriverStatus().equals(DriverStatus.FREE)) {
            driver.setDriverStatus(DriverStatus.TAKEN);
            driver.setCurrentDriving(driving);

            DrivingDTOWithLocations data = DrivingConverter.convertWithLocationsAndDriver(driving, drivingWithLocations.getLocations());

            navigationService.startNavigationForDriver(data, true);
            driverNotificationService.sendCurrentDrivingToDriver(data);
        } else {
            driver.setDriverStatus(DriverStatus.RESERVED);
            driver.setNextDriving(driving);

            DrivingDTOWithLocations data = DrivingConverter.convertWithLocationsAndDriver(driving, drivingWithLocations.getLocations());
            driverNotificationService.sendNextDrivingToDriver(data);
        }
    }

    @Override
    @Transactional
    public void onFailure(Driving driving) {
        List<String> passengersToSendNotifications = driving.getPassengers().stream()
                .map(Passenger::getEmail).collect(Collectors.toList());

        drivingRejectionService.rejectDriving(driving);
        notificationService.sendNotificationForPaymentFailure(passengersToSendNotifications);
    }
}
