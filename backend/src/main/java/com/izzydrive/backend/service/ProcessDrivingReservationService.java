package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.driving.DrivingRequestDTO;

public interface ProcessDrivingReservationService {
    void processReservation(DrivingRequestDTO request);
}
