package com.izzydrive.backend.service.drivingprocessing.regular;

import com.izzydrive.backend.dto.driving.DrivingRequestDTO;

public interface ProcessDrivingRegularService {

    void process(DrivingRequestDTO request);
}
