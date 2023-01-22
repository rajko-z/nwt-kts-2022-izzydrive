package com.izzydrive.backend.service.driving.routes;

import com.izzydrive.backend.dto.map.CalculatedRouteDTO;

public interface DrivingRoutesService {

    CalculatedRouteDTO getEstimatedRouteLeftToStartOfDriving(Long drivingId);
}
