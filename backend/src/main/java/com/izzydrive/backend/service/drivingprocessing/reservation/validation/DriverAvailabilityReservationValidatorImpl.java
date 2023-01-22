package com.izzydrive.backend.service.drivingprocessing.reservation.validation;

import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverLocker;
import com.izzydrive.backend.service.users.driver.locker.DriverLockerService;
import com.izzydrive.backend.service.users.driver.workingtime.validation.DriverWorkTimeValidationServiceImpl;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DriverAvailabilityReservationValidatorImpl implements DriverAvailabilityReservationValidator{

    private final DriverWorkTimeValidationServiceImpl driverWorkTimeValidationServiceImpl;

    private final DriverLockerService driverLockerService;

    @Override
    public void checkIfDriverIsStillAvailable(DrivingRequestDTO request, Driver driver) {
        if (!driver.isActive()) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        if(driver.getReservedFromClientDriving() != null){
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }

        DriverLocker driverLocker = this.driverLockerService.findByDriverEmail(driver.getEmail())
                .orElseThrow(()-> new BadRequestException(ExceptionMessageConstants.DRIVER_IS_AVAILABLE));

        if (driverLocker.getPassengerEmail() != null) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }

        boolean onTime = driverWorkTimeValidationServiceImpl.driverOnTimeForFutureDrivingReservation(
                driver,
                request.getDrivingFinderRequest().getStartLocation(),
                request.getDrivingFinderRequest().getScheduleTime()
        );

        if (!onTime) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }
}
