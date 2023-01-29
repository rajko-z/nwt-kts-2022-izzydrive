package com.izzydrive.backend.service.driving.validation;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.model.Driving;

import java.time.LocalDateTime;
import java.util.List;

public interface DrivingValidationService {
    void checkReservationScheduledTime(LocalDateTime scheduledTime);

    void validateDrivingFinderRequest(DrivingFinderRequestDTO request);

    void validateAllLocationsForSimpleRequest(List<AddressOnMapDTO> locations);

    boolean drivingExpiredForPayment(Driving driving);
}
