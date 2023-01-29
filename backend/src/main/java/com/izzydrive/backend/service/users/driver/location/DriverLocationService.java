package com.izzydrive.backend.service.users.driver.location;

import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.users.driver.DriverLocation;

public interface DriverLocationService {

    void updateCoordinatesForDriver(String driverEmail, double lat, double lon);

    LocationDTO getDriverLocation(String driverEmail);

    void save(DriverLocation driverLocation);
}
