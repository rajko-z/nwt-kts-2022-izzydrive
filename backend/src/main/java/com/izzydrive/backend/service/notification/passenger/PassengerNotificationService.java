package com.izzydrive.backend.service.notification.passenger;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;

import java.util.List;

public interface PassengerNotificationService {
    void sendSignalThatRideHasStart(List<String> passengerEmails);

    void sendRefreshedDriving(DrivingDTOWithLocations nextDriving);

    void sendSignalThatRideHasEnded(List<String> passengerEmails);

}
