package com.izzydrive.backend.service.notification;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DriverNotificationServiceImpl implements DriverNotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendSignalThatDriverArrivedAtStart(String driverEmail) {
        this.simpMessagingTemplate.convertAndSend("/driving/arrivedAtStart", driverEmail);
    }

    @Override
    public void sendCurrentDrivingToDriver(DrivingDTOWithLocations payload) {
        this.simpMessagingTemplate.convertAndSend("/driving/loadCurrentDriving", payload);
    }

    @Override
    public void sendNextDrivingToDriver(DrivingDTOWithLocations payload) {
        this.simpMessagingTemplate.convertAndSend("/driving/loadNextDriving", payload);
    }
}
