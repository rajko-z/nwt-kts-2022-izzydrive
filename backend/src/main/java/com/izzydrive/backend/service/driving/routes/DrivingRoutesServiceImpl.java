package com.izzydrive.backend.service.driving.routes;

import com.izzydrive.backend.converters.DrivingConverter;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.service.driving.DrivingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DrivingRoutesServiceImpl implements DrivingRoutesService {

    private final DrivingService drivingService;

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
            if (driver.getLon() == coordinates.get(i).getLon() && driver.getLat() == coordinates.get(i).getLat()) {
                coordinatesPassed = i;
                coordinatesLeft = coordinates.subList(i+1, coordinates.size());
            }
        }

        if (coordinatesPassed == 0) {
            return new CalculatedRouteDTO(new ArrayList<>(), 0, 0);
        }

        double proportion = coordinates.size() * 1.0 / coordinatesPassed;

        double estimatedDuration = fromDriverToStart.getDuration() / proportion;
        double estimatedDistance = fromDriverToStart.getDistance() / proportion;

        return new CalculatedRouteDTO(coordinatesLeft, estimatedDistance, estimatedDuration);
    }
}
