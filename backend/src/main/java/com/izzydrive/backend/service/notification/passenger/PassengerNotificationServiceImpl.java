package com.izzydrive.backend.service.notification.passenger;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PassengerNotificationServiceImpl implements PassengerNotificationService{

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendSignalThatRideHasStart(List<String> passengerEmails) {
        this.simpMessagingTemplate.convertAndSend("/driving/rideStarted", passengerEmails);
    }

    @Override
    public void sendRefreshedDriving(DrivingDTOWithLocations driving) {
        this.simpMessagingTemplate.convertAndSend("/driving/refreshedDrivingForPassengers", driving);
    }

    @Override
    public void sendSignalThatRideHasEnded(List<String> passengerEmails) {
        this.simpMessagingTemplate.convertAndSend("/driving/rideEnded", passengerEmails);
    }
}
