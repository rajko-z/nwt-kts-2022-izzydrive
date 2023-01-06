package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.converters.DriverDTOConverter;
import com.izzydrive.backend.dto.AddressOnMapDTO;
import com.izzydrive.backend.dto.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.service.CarService;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.service.users.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class DrivingFinderServiceImpl {

    private final DriverService driverService;

    private final CarService carService;

    private final MapService mapService;

    public List<DrivingOptionDTO> getSimpleDrivingOptions(AddressOnMapDTO startLocation, AddressOnMapDTO endLocation) {
        List<Driver> drivers = driverService
                .findAllActiveDrivers()
                .stream()
                .filter(d -> !d.getDriverStatus().equals(DriverStatus.RESERVED))
                .collect(Collectors.toList());

        List<DrivingOptionDTO> options = new ArrayList<>();

        for (Driver driver : drivers) {
            CalculatedRouteDTO routeFromDriverToStart = calculateRouteFromDriverToStartLocation(startLocation, driver);
            CalculatedRouteDTO routeFromStartToFinish = mapService.getCalculatedRoutesFromTwoCoords(startLocation, endLocation).get(0);

            DrivingOptionDTO drivingOptionDTO = new DrivingOptionDTO(
                    DriverDTOConverter.convertBasicWithCar(driver,carService),
                    new LocationDTO(driver.getLon(),driver.getLat()),
                    getDurationInMinutesFromSeconds(routeFromDriverToStart.getDuration()),
                    carService.calculatePrice(driver.getCar(),routeFromStartToFinish.getDistance()),
                    routeFromDriverToStart,
                    routeFromStartToFinish
            );
            options.add(drivingOptionDTO);
        }
        options.sort(Comparator.comparingDouble(o -> o.getDriverToStartPath().getDuration()));
        if (options.size() <= 5) {
            return options;
        }
        return options.subList(0,5);
    }

    private CalculatedRouteDTO calculateRouteFromDriverToStartLocation(AddressOnMapDTO startLocation, Driver driver) {
        AddressOnMapDTO driverLocation = new AddressOnMapDTO(driver.getLon(), driver.getLat());

        if (driver.getCurrentDriving() != null) {
            Address end = driver.getCurrentDriving().getRoute().getEnd();
            AddressOnMapDTO currentDrivingEndLocation = new AddressOnMapDTO(end.getLongitude(), end.getLatitude());

            CalculatedRouteDTO fromDriverToEndLocation = mapService.getCalculatedRoutesFromTwoCoords(driverLocation, currentDrivingEndLocation).get(0);
            CalculatedRouteDTO fromEndLocationToStart = mapService.getCalculatedRoutesFromTwoCoords(currentDrivingEndLocation, startLocation).get(0);

            List<LocationDTO> totalCoords = Stream
                    .concat(fromDriverToEndLocation.getCoordinates().stream(),
                            fromEndLocationToStart.getCoordinates().stream())
                    .collect(Collectors.toList());

            double totalDistance = fromDriverToEndLocation.getDistance() + fromEndLocationToStart.getDistance();
            double totalDuration = fromDriverToEndLocation.getDuration() + fromEndLocationToStart.getDuration();

            return new CalculatedRouteDTO(totalCoords, totalDistance, totalDuration);
        }

        return mapService.getCalculatedRoutesFromTwoCoords(driverLocation, startLocation).get(0);
    }

    private int getDurationInMinutesFromSeconds(double duration) {
        return (int)Math.ceil(duration / 60);
    }

}
