package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.driving.DrivingRequestDTO;

public interface ProcessDrivingRequestService {

    void process(DrivingRequestDTO request);
}
