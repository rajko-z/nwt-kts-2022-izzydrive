package com.izzydrive.backend.service.users.driver.location;

import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.users.driver.DriverLocation;
import com.izzydrive.backend.repository.DriverLocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DriverLocationServiceImpl implements DriverLocationService {

    private final DriverLocationRepository driverLocationRepository;

    @Override
    @Transactional
    public void updateCoordinatesForDriver(String driverEmail, double lat, double lon) {
        driverLocationRepository.updateCoordinatesForDriver(driverEmail, lat, lon);
    }

    @Override
    public LocationDTO getDriverLocation(String driverEmail) {
        DriverLocation location = driverLocationRepository.findByEmail(driverEmail);
        return new LocationDTO(location.getLon(), location.getLat());
    }
}
