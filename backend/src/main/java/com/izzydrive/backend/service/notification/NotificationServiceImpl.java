package com.izzydrive.backend.service.notification;


import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.Notification;
import com.izzydrive.backend.model.NotificationStatus;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.NotificationRepository;
import com.izzydrive.backend.service.users.UserService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final NotificationRepository notificationRepository;

    private final UserService userService;

    @Override
    public void sendNotificationNewReservationDriving(String email, Driving driving) {
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
        notificationDTO.setUserEmail(email);
        notificationDTO.setNotificationStatus(NotificationStatus.NEW_RESERVATION);
        this.simpMessagingTemplate.convertAndSend("/notification/newReservationDriving", notificationDTO);
        createAndSaveNotification(notificationDTO);
    }

    @Override
    public void sendNotificationNewDriving(String passengerEmail, Driving driving) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("You have been added to a new ride");
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
        notificationDTO.setNotificationStatus(NotificationStatus.NEW_DRIVING);
        this.simpMessagingTemplate.convertAndSend("/notification/newRide", notificationDTO);
        createAndSaveNotification(notificationDTO);
    }

    @Override
    public void sendNotificationRejectDriving(List<String> passengersToSendNotifications, String startLocation, String endLocation) {
        for (String passengerEmail : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("The ride was canceled when declined by the linked user");
            notificationDTO.setStartLocation(startLocation);
            notificationDTO.setEndLocation(endLocation);
            notificationDTO.setUserEmail(passengerEmail);
            notificationDTO.setNotificationStatus(NotificationStatus.REJECTED_DRIVING_PASSENGER);
            this.simpMessagingTemplate.convertAndSend("/notification/cancelRide", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationRejectDrivingFromDriver(String adminEmail, Driving driving, String driverEmail, String reason) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage(reason);
        notificationDTO.setDuration(driving.getDuration());
        notificationDTO.setPrice(driving.getPrice());
        notificationDTO.setStartLocation(driving.getRoute().getStart().getName());
        notificationDTO.setEndLocation(driving.getRoute().getEnd().getName());
        List<String> intermediateStationDTO = new ArrayList<>();
        for (Address intermediateStation : driving.getRoute().getIntermediateStations()) {
            intermediateStationDTO.add(intermediateStation.getName());
        }
        notificationDTO.setIntermediateLocations(intermediateStationDTO);
        notificationDTO.setUserEmail(adminEmail);
        notificationDTO.setDriverEmail(driverEmail);
        notificationDTO.setDrivingId(driving.getId());
        notificationDTO.setNotificationStatus(NotificationStatus.REJECTED_DRIVING_DRIVER);
        this.simpMessagingTemplate.convertAndSend("/notification/cancelRideDriver", notificationDTO);
        createAndSaveNotification(notificationDTO);
    }

    @Override
    public void sendNotificationForPaymentExpired(List<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Payment session has expired. Your current driving is canceled");
            notificationDTO.setUserEmail(passenger);
            notificationDTO.setNotificationStatus(NotificationStatus.PAYMENT_EXPIRED);
            this.simpMessagingTemplate.convertAndSend("/notification/paymentSessionExpired", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationForPaymentFailure(List<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Payment failure, canceling current driving. Make sure every passenger input correct paying info and have enough funds.");
            notificationDTO.setUserEmail(passenger);
            notificationDTO.setNotificationStatus(NotificationStatus.PAYMENT_FAILURE);
            this.simpMessagingTemplate.convertAndSend("/notification/paymentFailure", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationForPaymentSuccess(List<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Payment success.");
            notificationDTO.setUserEmail(passenger);
            notificationDTO.setNotificationStatus(NotificationStatus.PAYMENT_SUCCESS);
            this.simpMessagingTemplate.convertAndSend("/notification/paymentSuccess", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationNewDrivingDriver(String driverEmail) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("You have been assigned a new ride.");
        notificationDTO.setUserEmail(driverEmail);
        notificationDTO.setNotificationStatus(NotificationStatus.NEW_DRIVING_DRIVER);
        this.simpMessagingTemplate.convertAndSend("/notification/newRideDriver", notificationDTO);
        createAndSaveNotification(notificationDTO);
    }

    @Override
    public void sendNotificationCancelDriving(String passengerEmail, Driving driving) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("You reservations is canceled");
        notificationDTO.setStartLocation(driving.getRoute().getStart().getName());
        notificationDTO.setEndLocation(driving.getRoute().getEnd().getName());
        notificationDTO.setUserEmail(passengerEmail);
        notificationDTO.setNotificationStatus(NotificationStatus.REJECTED_RESERVATION);
        this.simpMessagingTemplate.convertAndSend("/notification/cancelReservation", notificationDTO);
        createAndSaveNotification(notificationDTO);
    }

    @Override
    public void sendNotificationDriverArrivedAtStartLocation(Collection<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Your driver arrived at start location");
            notificationDTO.setUserEmail(passenger);
            notificationDTO.setNotificationStatus(NotificationStatus.DRIVER_ARRIVED_AT_START);
            this.simpMessagingTemplate.convertAndSend("/notification/driverArrivedStart", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public List<NotificationDTO> findAll() {
        User user = userService.getCurrentlyLoggedUser();
        return notificationRepository.findAllByUserEmail(user.getEmail())
                .stream().map(NotificationDTO::new)
                .sorted((n1, n2) -> n2.getCreationDate().compareTo(n1.getCreationDate())).collect(Collectors.toList());
    }

    @Override
    public void deleteNotification(Long id) {
        Notification notification = this.notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.NOTIFICATION_DOESNT_EXIST));
        notificationRepository.delete(notification);
    }

    @Override
    public void deleteNotificationFromAdmin(Long drivingId) {
        User user = userService.getCurrentlyLoggedUser();
        Notification notification = this.notificationRepository.findByDrivingIdAndUserEmail(drivingId, user.getEmail())
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.NOTIFICATION_DOESNT_EXIST));
        notificationRepository.delete(notification);
    }

    private void createAndSaveNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setPrice(notificationDTO.getPrice());
        notification.setReservationDate(notificationDTO.getReservationTime());
        notification.setMessage(notificationDTO.getMessage());
        notification.setDuration(notificationDTO.getDuration());
        notification.setStartLocation(notificationDTO.getStartLocation());
        notification.setEndLocation(notificationDTO.getEndLocation());
        notification.setNotificationStatus(notificationDTO.getNotificationStatus());
        notification.setUserEmail(notificationDTO.getUserEmail());
        notification.setCreationDate(LocalDateTime.now());
        notification.setDrivingId(notificationDTO.getDrivingId());
        notificationRepository.save(notification);
    }
}