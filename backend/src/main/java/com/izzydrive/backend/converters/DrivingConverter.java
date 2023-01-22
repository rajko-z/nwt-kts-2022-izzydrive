package com.izzydrive.backend.converters;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.Location;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.service.users.driver.car.CarService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DrivingConverter {
    private DrivingConverter() {}

    public static DrivingDTOWithLocations convertWithLocationsAndDriver(Driving driving, List<Location> locations, CarService carService) {
        List<LocationDTO> fromDriverToStartLoc = new ArrayList<>();
        List<LocationDTO> fromStartToEndLoc = new ArrayList<>();

        for (Location l : locations) {
            if (l.isForDrive()) {
                fromStartToEndLoc.add(new LocationDTO(l.getLongitude(), l.getLatitude()));
            } else {
                fromDriverToStartLoc.add(new LocationDTO(l.getLongitude(), l.getLatitude()));
            }
        }
        CalculatedRouteDTO fromDriverToStart = new CalculatedRouteDTO(fromDriverToStartLoc, 0, 0);
        CalculatedRouteDTO fromStartToEnd = new CalculatedRouteDTO(fromStartToEndLoc, driving.getDistance(), driving.getDuration());

        return DrivingDTOWithLocations.builder()
                .id(driving.getId())
                .price(driving.getPrice())
                .startDate(driving.getStartDate())
                .endDate(driving.getEndDate())
                .creationTime(driving.getCreationDate())
                .route(RouteDTOConverter.convert(driving.getRoute()))
                .passengers(driving.getPassengers().stream().map(User::getEmail).collect(Collectors.toList()))
                .isReservation(driving.isReservation())
                .drivingState(driving.getDrivingState())
                .driver(DriverDTOConverter.convertBasicWithCar(driving.getDriver(), carService))
                .fromDriverToStart(fromDriverToStart)
                .fromStartToEnd(fromStartToEnd)
                .build();
    }

    public static CalculatedRouteDTO convertDrivingWithLocationsToCalculatedRouteDTO(Driving driving, boolean fromDriverToStart) {
        List<Location> locations = driving.getLocationsFromStartToEnd();
        if (fromDriverToStart) {
            locations = driving.getLocationsFromDriverToStart();
        }
        List<LocationDTO> coordinates = locations.stream()
                .map(l -> new LocationDTO(l.getLongitude(), l.getLatitude()))
                .collect(Collectors.toList());

        if (fromDriverToStart) {
            return new CalculatedRouteDTO(coordinates,driving.getDistanceFromDriverToStart(), driving.getDurationFromDriverToStart());
        }
        return new CalculatedRouteDTO(coordinates,driving.getDistance(), driving.getDuration());
    }
}