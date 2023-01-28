package com.izzydrive.backend.service.drivingprocessing.regular.validation;

import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.driver.DriverStatus;
import com.izzydrive.backend.service.users.driver.location.DriverLocationService;
import com.izzydrive.backend.service.users.driver.workingtime.validation.DriverWorkTimeValidationServiceImpl;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DriverAvailabilityRegularValidatorImpl implements DriverAvailabilityRegularValidator {

    private final DriverWorkTimeValidationServiceImpl driverWorkTimeValidationServiceImpl;

    private final DriverLocationService driverLocationService;

    @Override
    public void checkIfDriverIsStillAvailable(DrivingRequestDTO request, Driver driver, CalculatedRouteDTO fromDriverToStart) {
        if (!driver.isActive()) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        checkIfDriverIsStillOnTheSamePath(request.getDrivingOption(), driver);

        List<CalculatedRouteDTO> fromStartToEndRoutes = List.of(request.getDrivingOption().getStartToEndPath());

        if (!driverWillNotOutworkAndWillBeOnTimeForFutureDriving(
                fromDriverToStart,
                fromStartToEndRoutes.get(0),
                driver,
                request.getDrivingFinderRequest().getEndLocation()))
        {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }

    private boolean driverWillNotOutworkAndWillBeOnTimeForFutureDriving(CalculatedRouteDTO fromDriverToStart,
                                                                        CalculatedRouteDTO fromStartToEndRoute,
                                                                        Driver driver,
                                                                        AddressOnMapDTO endLocation)
    {
        return this.driverWorkTimeValidationServiceImpl.driverNotOutwork(fromDriverToStart, fromStartToEndRoute, driver)
                &&
                this.driverWorkTimeValidationServiceImpl.driverOnTimeForFutureDrivingRegular(fromDriverToStart, fromStartToEndRoute, driver, endLocation);
    }

    private void checkIfDriverIsStillOnTheSamePath(DrivingOptionDTO option, Driver driver) {
        DriverStatus currStat = driver.getDriverStatus();
        DriverStatus prevStat = option.getDriver().getDriverStatus();
        List<LocationDTO> previousLocations = new ArrayList<>(List.of(option.getDriverCurrentLocation()));
        previousLocations.addAll(option.getDriverToStartPath().getCoordinates());

        if (currStat.equals(DriverStatus.RESERVED)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        if (currStat.equals(DriverStatus.FREE) && driverCurrentLocationNotInLocations(driver, previousLocations)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        if (prevStat.equals(DriverStatus.FREE) && !currStat.equals(DriverStatus.FREE)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        if (prevStat.equals(DriverStatus.ACTIVE)) {
            checkWhenPreviousDriverStateIsActive(driver, previousLocations);
        }
        if (prevStat.equals(DriverStatus.TAKEN)) {
            checkWhenPreviousDriverStateIsTaken(driver, previousLocations);
        }
    }

    private void checkWhenPreviousDriverStateIsActive(Driver driver, List<LocationDTO> locations) {
        DriverStatus currStat = driver.getDriverStatus();

        if (currStat.equals(DriverStatus.TAKEN)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        if (currStat.equals(DriverStatus.ACTIVE) && driverIsNoLongerActiveOnCurrentDriving(driver, locations)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }

    private void checkWhenPreviousDriverStateIsTaken(Driver driver, List<LocationDTO> locations) {
        DriverStatus currStat = driver.getDriverStatus();

        if ((currStat.equals(DriverStatus.TAKEN) || currStat.equals(DriverStatus.ACTIVE)) &&
                driverIsNoLongerActiveOnCurrentDriving(driver, locations)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }

    private boolean driverCurrentLocationNotInLocations(Driver driver, List<LocationDTO> locations) {
        LocationDTO currLoc = driverLocationService.getDriverLocation(driver.getEmail());
        return !locationPresentInLocations(currLoc, locations);
    }

    private boolean locationPresentInLocations(LocationDTO loc, List<LocationDTO> locations) {
        for (LocationDTO location : locations) {
            if (loc.getLat() == location.getLat() && loc.getLon() == location.getLon()) {
                return true;
            }
            if (Math.abs(loc.getLat() - location.getLat()) <= 0.002 && Math.abs(loc.getLon() - location.getLon()) <= 0.002) {
                return true;
            }
        }
        return false;
    }

    private boolean driverIsNoLongerActiveOnCurrentDriving(Driver driver, List<LocationDTO> locations) {
        LocationDTO currLoc = driverLocationService.getDriverLocation(driver.getEmail());
        Address endAddress = driver.getCurrentDriving().getRoute().getEnd();
        LocationDTO endLoc = new LocationDTO(endAddress.getLongitude(), endAddress.getLatitude());

        return !locationPresentInLocations(currLoc, locations) ||
                !locationPresentInLocations(endLoc, locations);
    }

}
