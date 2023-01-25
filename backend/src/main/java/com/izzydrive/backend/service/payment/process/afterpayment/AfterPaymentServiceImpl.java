package com.izzydrive.backend.service.payment.process.afterpayment;

import com.izzydrive.backend.converters.DrivingConverter;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.Location;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.driving.rejection.DrivingRejectionService;
import com.izzydrive.backend.service.navigation.NavigationService;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.notification.driver.DriverNotificationService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.driver.locker.DriverLockerService;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    private final DriverService driverService;

    private final DriverRoutesService driverRoutesService;

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
        driverService.refresh(driver);

        setFromDriverToStartPathForDriving(driving);

        if (driver.getDriverStatus().equals(DriverStatus.FREE)) {
            driver.setDriverStatus(DriverStatus.TAKEN);
            driver.setCurrentDriving(driving);

            DrivingDTOWithLocations data = DrivingConverter.convertWithLocationsAndDriver(driving, driving.getLocations());
            navigationService.startNavigationForDriver(data, true);
            driverNotificationService.sendCurrentDrivingToDriver(data);
        } else {
            driver.setDriverStatus(DriverStatus.RESERVED);
            driver.setNextDriving(driving);

            DrivingDTOWithLocations data = DrivingConverter.convertWithLocationsAndDriver(driving, driving.getLocations());
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

    private void setFromDriverToStartPathForDriving(Driving driving) {
        Address start = driving.getRoute().getStart();
        AddressOnMapDTO startLocation = new AddressOnMapDTO(start.getLongitude(), start.getLatitude(), start.getName());
        CalculatedRouteDTO fromDriverToStart =
                driverRoutesService.getCalculatedRouteFromDriverToStart(driving.getDriver().getEmail(), startLocation);

        List<Location> updatedCoordinatesForDriving = getListOfUpdatedCoordinatesForDriving(driving, fromDriverToStart.getCoordinates());

        driving.setLocations(updatedCoordinatesForDriving);
        driving.setDistanceFromDriverToStart(fromDriverToStart.getDistance());
        driving.setDurationFromDriverToStart(fromDriverToStart.getDuration());
    }


    private List<Location> getListOfUpdatedCoordinatesForDriving(Driving driving, List<LocationDTO> fromDriverToStartLocationsDTOs) {
        List<Location> fromDriverToStartLocations = fromDriverToStartLocationsDTOs.stream()
                .map(l -> new Location(l.getLat(), l.getLon(), false)).collect(Collectors.toList());

        Driving drivingWithLocations = drivingService.getDrivingWithLocations(driving.getId());
        List<Location> fromStartToEndLocations = drivingWithLocations.getLocationsFromStartToEnd();

        List<Location> retVal = new ArrayList<>();
        retVal.addAll(fromDriverToStartLocations);
        retVal.addAll(fromStartToEndLocations);

        return retVal;
    }
}
