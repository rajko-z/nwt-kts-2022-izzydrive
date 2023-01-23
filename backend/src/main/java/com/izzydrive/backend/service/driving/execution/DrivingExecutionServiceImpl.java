package com.izzydrive.backend.service.driving.execution;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.service.navigation.NavigationService;
import com.izzydrive.backend.service.notification.passenger.PassengerNotificationService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class DrivingExecutionServiceImpl implements DrivingExecutionService{

    private final DriverService driverService;

    private final NavigationService navigationService;

    private final PassengerNotificationService passengerNotificationService;

    @Override
    @Transactional
    public void startDriving() {
        Driver driver = driverService.getCurrentlyLoggedDriverWithCurrentDriving();
        DrivingDTOWithLocations driving = driverService.getCurrentDriving();
        if (driving == null || !driving.getDrivingState().equals(DrivingState.WAITING)) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_DO_NOT_HAVE_CURRENT_WAITING_DRIVING_TO_START);
        }
        if (!driverArrivedAtStartLocation(driving)) {
            throw new BadRequestException(ExceptionMessageConstants.CANT_START_DRIVING_NOT_AT_LOCATION);
        }
        if (driver.getDriverStatus().equals(DriverStatus.TAKEN)) {
            driver.setDriverStatus(DriverStatus.ACTIVE);
        }
        driver.getCurrentDriving().setDrivingState(DrivingState.ACTIVE);
        driver.getCurrentDriving().setStartDate(LocalDateTime.now());
        passengerNotificationService.sendSignalThatRideHasStart(driving.getPassengers());
        navigationService.startNavigationForDriver(driving, false);
    }

    private boolean driverArrivedAtStartLocation(DrivingDTOWithLocations driving) {
        List<LocationDTO> coordinates = driving.getFromDriverToStart().getCoordinates();
        LocationDTO expectedCoordinate = coordinates.get(coordinates.size() - 1);
        LocationDTO driverCoordinate = driving.getDriver().getLocation();

        return expectedCoordinate.getLat() == driverCoordinate.getLat() &&
               expectedCoordinate.getLon() == driverCoordinate.getLon();
    }

    @Override
    @Transactional
    public void endDriving() {
        //TODO::
    }
}
