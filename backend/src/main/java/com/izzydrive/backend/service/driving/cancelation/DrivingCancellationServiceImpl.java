package com.izzydrive.backend.service.driving.cancelation;

import com.izzydrive.backend.dto.CancellationReasonDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.driving.execution.DrivingExecutionService;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.notification.driver.DriverNotificationService;
import com.izzydrive.backend.service.users.admin.AdminService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DrivingCancellationServiceImpl implements DrivingCancellationService {

    private final DriverService driverService;

    private final DrivingService drivingService;

    private final DrivingExecutionService drivingExecutionService;

    private final DriverNotificationService driverNotificationService;

    private final NotificationService notificationService;

    private final AdminService adminService;

    private final PassengerService passengerService;

    @Override
    @Transactional
    public void cancelRegularDriving(CancellationReasonDTO cancellationReasonDTO) {
        Driver driver = driverService.getCurrentlyLoggedDriverWithCurrentDriving();
        Driving driving = drivingService.findById(cancellationReasonDTO.getDrivingId())
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST));

        boolean cancellationPossible = false;

        if (driverWantsToCancelCurrentDriving(driver, driving)) {
            drivingExecutionService.stopCurrentDrivingAndMoveToNextIfExist(driver);
            cancellationPossible = true;
        }
        else if (driverWantsToCancelNextDriving(driver, driving)) {
            changeDriverStatusAfterCancellingNextDriving(driver);
            cancellationPossible = true;
        }

        if (cancellationPossible) {
            passengerService.deleteCurrentDrivingFromPassengers(driving.getPassengers());
            drivingService.deleteDriving(driving.getId());
            notificationService.sendNotificationRejectDrivingFromDriverToAdmin(adminService.findAdmin().getEmail(), driving, driver.getEmail(), cancellationReasonDTO.getText());
            notificationService.sendNotificationRejectDrivingFromDriverToPassengers(driving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
        } else {
            throw new BadRequestException(ExceptionMessageConstants.CANT_FIND_DRIVING_TO_CANCEL);
        }
    }

    private void changeDriverStatusAfterCancellingNextDriving(Driver driver) {
        if (driver.getCurrentDriving().getStartDate() != null) {
            driver.setDriverStatus(DriverStatus.ACTIVE);
        } else {
            driver.setDriverStatus(DriverStatus.TAKEN);
        }
        driver.setNextDriving(null);
        driverNotificationService.deleteNextDrivingSignal(driver.getEmail());
    }

    private boolean driverWantsToCancelCurrentDriving(Driver driver, Driving driving) {
        return driver.getCurrentDriving() != null &&
               driver.getCurrentDriving().getId().equals(driving.getId()) &&
               driver.getCurrentDriving().getDrivingState().equals(DrivingState.WAITING);
    }

    private boolean driverWantsToCancelNextDriving(Driver driver, Driving driving) {
        return driver.getNextDriving() != null &&
               driver.getNextDriving().getId().equals(driving.getId()) &&
               driver.getNextDriving().getDrivingState().equals(DrivingState.WAITING);
    }
}
