package com.izzydrive.backend.service.notification;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;

public interface DriverNotificationService {
    void sendSignalThatDriverArrivedAtStart(String driverEmail);

    void sendCurrentDrivingToDriver(DrivingDTOWithLocations drivingDTOWithLocations);

    void sendNextDrivingToDriver(DrivingDTOWithLocations drivingDTOWithLocations);
}
