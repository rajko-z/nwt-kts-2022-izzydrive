package com.izzydrive.backend.service.users.driver.routes;

import com.izzydrive.backend.converters.DrivingConverter;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.driver.location.DriverLocationService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class DriverRoutesServiceImpl implements DriverRoutesService {

    private final DriverService driverService;

    private final MapService mapService;

    private final DriverLocationService driverLocationService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CalculatedRouteDTO getCalculatedRouteFromDriverToStart(String driverEmail, AddressOnMapDTO startLocation){
        Driver driver = this.driverService.findByEmailWithCurrentNextAndReservedDriving(driverEmail)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail)));

        LocationDTO driverLoc = driverLocationService.getDriverLocation(driver.getEmail());
        if (driver.getCurrentDriving() == null) {
            AddressOnMapDTO driverLocation = new AddressOnMapDTO(driverLoc.getLon(), driverLoc.getLat());
            return mapService.getCalculatedRoutesFromPoints(Arrays.asList(driverLocation, startLocation)).get(0);
        }

        CalculatedRouteDTO getEstimatedRouteLeft = this.getEstimatedRouteLeftForCurrentDriving(driverEmail);
        Address tmp = driver.getCurrentDriving().getRoute().getEnd();
        AddressOnMapDTO currDrivingEndLocation = new AddressOnMapDTO(tmp.getLongitude(), tmp.getLatitude());

        if (driver.getNextDriving() == null) {
            CalculatedRouteDTO getRouteFromCurrDrivingEndToStart = mapService
                    .getCalculatedRoutesFromPoints(Arrays.asList(currDrivingEndLocation, startLocation)).get(0);

            return mapService.concatRoutesIntoOne(Arrays.asList(getEstimatedRouteLeft, getRouteFromCurrDrivingEndToStart));
        }

        CalculatedRouteDTO routeWithNextDriving = this.getCalculatedRouteWithNextDriving(startLocation, driverEmail, currDrivingEndLocation);
        return mapService.concatRoutesIntoOne(Arrays.asList(getEstimatedRouteLeft, routeWithNextDriving));
    }

    private CalculatedRouteDTO getCalculatedRouteWithNextDriving(AddressOnMapDTO startLocation, String driverEmail, AddressOnMapDTO currDrivingEndLocation) {
        Driver driver = this.driverService.findByEmailWithNextDrivingAndLocations(driverEmail)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail)));

        Address nextAddress = driver.getNextDriving().getRoute().getStart();
        AddressOnMapDTO nextDrivingStartLocation = new AddressOnMapDTO(nextAddress.getLongitude(), nextAddress.getLatitude());

        Address nextEndAddress = driver.getNextDriving().getRoute().getEnd();
        AddressOnMapDTO nextDrivingEndLocation = new AddressOnMapDTO(nextEndAddress.getLongitude(), nextEndAddress.getLatitude());

        return  mapService
                .getCalculatedRoutesFromPoints(Arrays.asList(currDrivingEndLocation, nextDrivingStartLocation, nextDrivingEndLocation, startLocation)).get(0);
    }

    @Override
    public CalculatedRouteDTO getEstimatedRouteLeftForCurrentDriving(String driverEmail) {
        Driver driver = this.driverService.findByEmailWithCurrentDrivingAndLocations(driverEmail)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail)));

        if (driver.getCurrentDriving() == null) {
            return new CalculatedRouteDTO(new ArrayList<>(), 0, 0);
        }

        Driving currDriving = driver.getCurrentDriving();

        if (!currDriving.getDrivingState().equals(DrivingState.FINISHED) &&
                !currDriving.getDrivingState().equals(DrivingState.ACTIVE)) {
            return getEstimatedRouteLeftForDrivingThatDidNotStartYet(driver);
        }
        else if (currDriving.getDrivingState().equals(DrivingState.ACTIVE)) {
            return getEstimatedRouteLeftForActiveDriving(driver);
        }

        return new CalculatedRouteDTO(new ArrayList<>(), 0, 0);
    }

    private CalculatedRouteDTO getEstimatedRouteLeftForDrivingThatDidNotStartYet(Driver driver) {
        LocationDTO driverLoc = driverLocationService.getDriverLocation(driver.getEmail());
        AddressOnMapDTO driverLocation = new AddressOnMapDTO(driverLoc.getLon(), driverLoc.getLat());
        Address tmp = driver.getCurrentDriving().getRoute().getStart();
        AddressOnMapDTO startLocation = new AddressOnMapDTO(tmp.getLongitude(), tmp.getLatitude());

        CalculatedRouteDTO fromDriverToStart = mapService
                .getCalculatedRoutesFromPoints(Arrays.asList(driverLocation, startLocation)).get(0);

        CalculatedRouteDTO fromStartToEnd =
                DrivingConverter.convertDrivingWithLocationsToCalculatedRouteDTO(
                        driver.getCurrentDriving(), false);

        return mapService.concatRoutesIntoOne(Arrays.asList(fromDriverToStart, fromStartToEnd));
    }

    private CalculatedRouteDTO getEstimatedRouteLeftForActiveDriving(Driver driver) {
        LocationDTO driverLoc = driverLocationService.getDriverLocation(driver.getEmail());
        AddressOnMapDTO driverLocation = new AddressOnMapDTO(driverLoc.getLon(), driverLoc.getLat());
        Address tmp = driver.getCurrentDriving().getRoute().getEnd();
        AddressOnMapDTO endLocation = new AddressOnMapDTO(tmp.getLongitude(), tmp.getLatitude());

        return mapService.getCalculatedRoutesFromPoints(Arrays.asList(driverLocation, endLocation)).get(0);
    }

    @Override
    public CalculatedRouteDTO getRouteFromEndLocationToStartOfFutureDriving(AddressOnMapDTO endLocation, Driver driver) {
        Address tmp = driver.getReservedFromClientDriving().getRoute().getStart();
        AddressOnMapDTO startFutureLocation = new AddressOnMapDTO(tmp.getLongitude(), tmp.getLatitude());

        return mapService.getCalculatedRoutesFromPoints(Arrays.asList(endLocation, startFutureLocation)).get(0);
    }

    @Override
    public CalculatedRouteDTO getCurrentRouteFromDriverLocationToStart(Driver driver, AddressOnMapDTO startLocation) {
        LocationDTO driverLoc = driverLocationService.getDriverLocation(driver.getEmail());
        AddressOnMapDTO firstPoint = new AddressOnMapDTO(driverLoc.getLon(), driverLoc.getLat());
        return mapService.getCalculatedRoutesFromPoints(List.of(firstPoint, startLocation)).get(0);
    }
}
