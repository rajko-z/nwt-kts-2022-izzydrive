package com.izzydrive.backend.service.notification.passenger;

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
}
