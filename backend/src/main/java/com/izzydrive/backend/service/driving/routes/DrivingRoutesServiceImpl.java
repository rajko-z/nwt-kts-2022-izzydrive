package com.izzydrive.backend.service.driving.routes;

import com.izzydrive.backend.converters.DrivingConverter;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.users.driver.location.DriverLocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DrivingRoutesServiceImpl implements DrivingRoutesService {

    private final DrivingService drivingService;

    private final DriverLocationService driverLocationService;

    @Override
    public CalculatedRouteDTO getEstimatedRouteLeftToStartOfDriving(Long drivingId) {
        Driving driving = this.drivingService.findByIdWithLocationsAndDriver(drivingId);

        Driver driver = driving.getDriver();
        CalculatedRouteDTO fromDriverToStart =
                DrivingConverter.convertDrivingWithLocationsToCalculatedRouteDTO(
                        driving, true);

        List<LocationDTO> coordinates = fromDriverToStart.getCoordinates();
        List<LocationDTO> coordinatesLeft = new ArrayList<>();
        int coordinatesPassed = 0;

        for (int i = 0; i < coordinates.size(); ++i) {
            LocationDTO driverLoc = driverLocationService.getDriverLocation(driver.getEmail());
            if (driverLoc.getLon() == coordinates.get(i).getLon() && driverLoc.getLat() == coordinates.get(i).getLat()) {
                coordinatesPassed = i;
                coordinatesLeft = coordinates.subList(i+1, coordinates.size());
                break;
            }
        }

        if (coordinatesPassed == 0) {
            return fromDriverToStart;
        }

        double proportion = coordinates.size() * 1.0 / coordinatesPassed;

        double durationPassed = fromDriverToStart.getDuration() / proportion;
        double distancePassed = fromDriverToStart.getDistance() / proportion;

        double durationLeft = fromDriverToStart.getDuration() - durationPassed;
        double distanceLeft = fromDriverToStart.getDistance() - distancePassed;

        return new CalculatedRouteDTO(coordinatesLeft, distanceLeft, durationLeft);
    }
}
