package com.izzydrive.backend.service.impl;


import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;


    @Override
    public void sendNotificationNewReservationDriving(String passengerEmail, Driving driving) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("You have a new reservation");
        notificationDTO.setDuration(driving.getDuration());
        notificationDTO.setPrice(driving.getPrice());
        notificationDTO.setStartLocation(driving.getRoute().getStart().getName());
        notificationDTO.setEndLocation(driving.getRoute().getEnd().getName());
        List<String> intermediateStationDTO = new ArrayList<>();
        for (Address intermediateStation : driving.getRoute().getIntermediateStations()) {
            intermediateStationDTO.add(intermediateStation.getName());
        }
        notificationDTO.setIntermediateLocations(intermediateStationDTO);
        notificationDTO.setReservationTime(driving.getReservationDate());
        notificationDTO.setUserEmail(passengerEmail);
        this.simpMessagingTemplate.convertAndSend("/notification/newReservationDriving", notificationDTO);
    }

    @Override
    public void sendNotificationNewDriving(String passengerEmail, Driving driving) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("You have been added to a new ride");
        notificationDTO.setDrivingId(driving.getId());
        notificationDTO.setDuration(driving.getDuration());
        notificationDTO.setPrice(driving.getPrice());
        notificationDTO.setStartLocation(driving.getRoute().getStart().getName());
        notificationDTO.setEndLocation(driving.getRoute().getEnd().getName());
        List<String> intermediateStationDTO = new ArrayList<>();
        for (Address intermediateStation : driving.getRoute().getIntermediateStations()) {
            intermediateStationDTO.add(intermediateStation.getName());
        }
        notificationDTO.setIntermediateLocations(intermediateStationDTO);
        notificationDTO.setUserEmail(passengerEmail);
        this.simpMessagingTemplate.convertAndSend("/notification/newRide", notificationDTO);
    }

    @Override
    public void sendNotificationRejectDriving(String passengerEmail, String startLocation, String endLocation) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("The ride was canceled when declined by the linked user");
        notificationDTO.setStartLocation(startLocation);
        notificationDTO.setEndLocation(endLocation);
        notificationDTO.setUserEmail(passengerEmail);
        this.simpMessagingTemplate.convertAndSend("/notification/cancelRide", notificationDTO);
    }

    @Override
    public void sendNotificationRejectDrivingFromDriver(String adminEmail){
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("The ride was cancelled!");
        notificationDTO.setUserEmail(adminEmail);
        this.simpMessagingTemplate.convertAndSend("/notification/cancelRideDriver", notificationDTO);
    }
}
