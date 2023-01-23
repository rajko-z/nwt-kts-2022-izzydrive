package com.izzydrive.backend.service.navigation;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;

public interface NavigationService {
    void startNavigationForDriver(DrivingDTOWithLocations drivingDTOWithLocations, boolean fromDriverToStart);
}
