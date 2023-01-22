package com.izzydrive.backend.service.drivingprocessing.regular.validation;

import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.model.users.Driver;

public interface DriverAvailabilityRegularValidator {

    void checkIfDriverIsStillAvailable(DrivingRequestDTO request, Driver driver, CalculatedRouteDTO fromDriverToStart);
}
