package com.izzydrive.backend.service.notification.driver;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;

public interface DriverNotificationService {
    void sendSignalThatDriverArrivedAtStart(String driverEmail);

    void sendCurrentDrivingToDriver(DrivingDTOWithLocations drivingDTOWithLocations);

    void sendNextDrivingToDriver(DrivingDTOWithLocations drivingDTOWithLocations);

    void sendReservationToDriver(DrivingDTOWithLocations drivingDTOWithLocations);

    void deleteReservationSignal(String driverEmail);

    void deleteCurrentDrivingSignal(String driverEmail);

    void deleteNextDrivingSignal(String driverEmail);
}
