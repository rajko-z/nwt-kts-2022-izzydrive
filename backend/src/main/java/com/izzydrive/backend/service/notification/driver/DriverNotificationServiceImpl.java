package com.izzydrive.backend.service.notification.driver;

import com.izzydrive.backend.dto.DriverDTO;
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

    @Override
    public void sendReservationToDriver(DrivingDTOWithLocations payload) {
        this.simpMessagingTemplate.convertAndSend("/driving/loadReservation", payload);
    }

    @Override
    public void deleteCurrentDrivingSignal(String driverEmail) {
        DrivingDTOWithLocations payload = new DrivingDTOWithLocations();
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setEmail(driverEmail);
        payload.setDriver(driverDTO);
        sendCurrentDrivingToDriver(payload);
    }

    @Override
    public void deleteNextDrivingSignal(String driverEmail) {
        DrivingDTOWithLocations payload = new DrivingDTOWithLocations();
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setEmail(driverEmail);
        payload.setDriver(driverDTO);
        sendNextDrivingToDriver(payload);
    }

    @Override
    public void deleteReservationSignal(String driverEmail) {
        DrivingDTOWithLocations payload = new DrivingDTOWithLocations();
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setEmail(driverEmail);
        payload.setDriver(driverDTO);
        sendReservationToDriver(payload);
    }
}
