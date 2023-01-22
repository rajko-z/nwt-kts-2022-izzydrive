package com.izzydrive.backend.service.users.driver.routes;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.model.users.Driver;

public interface DriverRoutesService {
    CalculatedRouteDTO getCalculatedRouteFromDriverToStart(String driverEmail, AddressOnMapDTO startLocation);

    CalculatedRouteDTO getEstimatedRouteLeftForCurrentDriving(String driverEmail);

    CalculatedRouteDTO getRouteFromEndLocationToStartOfFutureDriving(AddressOnMapDTO endLocation, Driver driver);
}
