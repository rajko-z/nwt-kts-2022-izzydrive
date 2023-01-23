package com.izzydrive.backend.service.notification;

import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.model.Driving;

import java.util.Collection;
import java.util.List;

public interface NotificationService {

    void sendNotificationNewReservationDriving(String passengerEmail, Driving driving);

    void sendNotificationNewDriving(String passengerEmail, Driving driving);

    void sendNotificationRejectDriving(List<String> passengersToSendNotifications, String startLocation, String endLocation);

    void sendNotificationRejectDrivingFromDriver(String adminEmail, Driving driving, String driverEmail,  String reason);

    void sendNotificationForPaymentExpired(List<String> passengersToSendNotifications);

    void sendNotificationForPaymentFailure(List<String> passengersToSendNotifications);

    void sendNotificationForPaymentSuccess(List<String> passengersToSendNotifications);

    void sendNotificationCancelDriving(String passengerEmail, Driving driving);

    void sendNotificationNewDrivingDriver(String driverEmail);

    void sendNotificationDriverArrivedAtStartLocation(Collection<String> passengersToSendNotifications);

    List<NotificationDTO> findAll();

    void deleteNotification(Long id);

    void deleteNotificationFromAdmin(Long drivingId);
}
