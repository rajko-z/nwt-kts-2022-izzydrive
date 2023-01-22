package com.izzydrive.backend.service.drivingprocessing.reservation;

import com.izzydrive.backend.dto.driving.DrivingRequestDTO;

public interface ProcessDrivingReservationService {
    void processReservation(DrivingRequestDTO request);
}
