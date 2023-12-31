package com.izzydrive.backend.service.driving.execution;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.Location;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.driver.DriverStatus;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.navigation.NavigationService;
import com.izzydrive.backend.service.notification.driver.DriverNotificationService;
import com.izzydrive.backend.service.notification.passenger.PassengerNotificationService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DrivingExecutionServiceImpl implements DrivingExecutionService{

    private final DriverService driverService;

    private final NavigationService navigationService;

    private final PassengerNotificationService passengerNotificationService;

    private final PassengerService passengerService;

    private final DriverNotificationService driverNotificationService;

    private final DriverRoutesService driverRoutesService;

    private final DrivingService drivingService;

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

        return (expectedCoordinate.getLat() == driverCoordinate.getLat() &&
               expectedCoordinate.getLon() == driverCoordinate.getLon()) || coordinates.size() <= 2;
    }

    @Override
    @Transactional
    public void endDriving() {
        Driver driver = driverService.getCurrentlyLoggedDriverWithCurrentDriving();
        Driving driving = driver.getCurrentDriving();
        if (driving == null || !driving.getDrivingState().equals(DrivingState.ACTIVE)) {
            throw new BadRequestException(ExceptionMessageConstants.CANT_FINISH_DRIVING_THAT_NOT_ACTIVE);
        }

        driving.setDrivingState(DrivingState.FINISHED);
        driving.setEndDate(LocalDateTime.now());

        passengerService.deleteCurrentDrivingFromPassengers(driving.getPassengers());
        passengerService.addNewDrivingToPassengersDrivings(driving.getPassengers(), driving);

        stopCurrentDrivingAndMoveToNextIfExist(driver);

        passengerNotificationService.sendSignalThatRideHasEnded(
                driving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList())
        );
    }

    @Override
    @Transactional
    public void stopCurrentDrivingAndMoveToNextIfExist(Driver driver) {
        navigationService.stopNavigationForDriver(driver.getEmail());

        if (driver.getNextDriving() == null) {
            driver.setDriverStatus(DriverStatus.FREE);
            driver.setCurrentDriving(null);
            driverNotificationService.deleteCurrentDrivingSignal(driver.getEmail());
        } else {
            driver.setDriverStatus(DriverStatus.TAKEN);

            DrivingDTOWithLocations nextDriving = drivingService.findDrivingWithLocationsDTOById(driver.getNextDriving().getId());
            CalculatedRouteDTO refreshedDriverToStartRoute = driverRoutesService
                    .getCurrentRouteFromDriverLocationToStart(driver, nextDriving.getRoute().getStart());
            nextDriving.setFromDriverToStart(refreshedDriverToStartRoute);

            Driving driving = getDrivingWithRefreshedDriverToStartRoute(driver.getNextDriving().getId(), refreshedDriverToStartRoute);

            driver.setCurrentDriving(driving);
            driverNotificationService.sendCurrentDrivingToDriver(nextDriving);

            driver.setNextDriving(null);
            driverNotificationService.deleteNextDrivingSignal(driver.getEmail());

            passengerNotificationService.sendRefreshedDriving(nextDriving);
            navigationService.startNavigationForDriver(nextDriving, true);
        }
    }

    private Driving getDrivingWithRefreshedDriverToStartRoute(Long drivingId, CalculatedRouteDTO refreshedDriverToStartRoute) {
        Driving driving = drivingService.getDrivingWithLocations(drivingId);

        List<Location> newLocations = new ArrayList<>();
        newLocations.addAll(refreshedDriverToStartRoute.getCoordinates().stream()
                .map(l -> new Location(l.getLat(), l.getLon(), false)).collect(Collectors.toList()));
        newLocations.addAll(driving.getLocationsFromStartToEnd());

        driving.setLocations(newLocations);
        driving.setDurationFromDriverToStart(refreshedDriverToStartRoute.getDuration());
        driving.setDistance(refreshedDriverToStartRoute.getDistance());

        return driving;
    }

}
