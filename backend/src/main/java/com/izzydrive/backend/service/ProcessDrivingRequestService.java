package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.model.Route;

public interface ProcessDrivingRequestService {

    void process(DrivingRequestDTO request);
    Route getRouteFromRequest(DrivingFinderRequestDTO request);
}
