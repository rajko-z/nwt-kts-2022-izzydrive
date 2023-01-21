package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.model.Driving;

import java.util.List;

public interface NotificationService {

    void sendNotificationNewReservationDriving(String passengerEmail, Driving driving);

    void sendNotificationNewDriving(String passengerEmail, Driving driving);

    void sendNotificationRejectDriving(String passengerEmail, String startLocation, String endLocation);

    void sendNotificationRejectDrivingFromDriver(String adminEmail, Driving driving, String driverEmail,  String reason);

    void sendNotificationForPaymentExpired(List<String> passengersToSendNotifications);

    void sendNotificationForPaymentFailure(List<String> passengersToSendNotifications);

    void sendNotificationForPaymentSuccess(List<String> passengersToSendNotifications);

    void sendNotificationCancelDriving(String passengerEmail, Driving driving);

    void sendNotificationNewDrivingDriver(String driverEmail);

    List<NotificationDTO> findAll();

    void deleteNotification(Long id);

    void deleteNotificationFromAdmin(Long drivingId);
}
