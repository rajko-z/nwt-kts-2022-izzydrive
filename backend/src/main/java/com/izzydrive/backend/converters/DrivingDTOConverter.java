package com.izzydrive.backend.converters;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.Location;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.service.CarService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DrivingDTOConverter {
    private DrivingDTOConverter() {}

    public static DrivingDTOWithLocations convertWithLocationsAndDriver(Driving driving, List<Location> locations, CarService carService) {
        List<LocationDTO> fromDriverToStart = new ArrayList<>();
        List<LocationDTO> fromStartToEnd = new ArrayList<>();

        for (Location l : locations) {
            if (l.isForDrive()) {
                fromStartToEnd.add(new LocationDTO(l.getLongitude(), l.getLatitude()));
            } else {
                fromDriverToStart.add(new LocationDTO(l.getLongitude(), l.getLatitude()));
            }
        }

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
                .duration(driving.getDuration())
                .distance(driving.getDistance())
                .driver(DriverDTOConverter.convertBasicWithCar(driving.getDriver(), carService))
                .fromDriverToStart(fromDriverToStart)
                .fromStartToEnd(fromStartToEnd)
                .build();
    }
}
