package com.izzydrive.backend.service.users.driver.location;

import com.izzydrive.backend.dto.map.LocationDTO;
import org.springframework.transaction.annotation.Transactional;

public interface DriverLocationService {

    @Transactional
    void updateCoordinatesForDriver(String driverEmail, double lat, double lon);

    LocationDTO getDriverLocation(String driverEmail);
}
