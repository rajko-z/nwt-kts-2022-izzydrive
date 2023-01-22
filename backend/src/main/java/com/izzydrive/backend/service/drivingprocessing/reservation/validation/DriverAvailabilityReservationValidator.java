package com.izzydrive.backend.service.drivingprocessing.reservation.validation;

import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.model.users.Driver;

public interface DriverAvailabilityReservationValidator {
    void checkIfDriverIsStillAvailable(DrivingRequestDTO request, Driver driver);
}
