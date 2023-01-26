package com.izzydrive.backend.service.notification;

import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.User;

import java.util.Collection;
import java.util.List;

public interface NotificationService {

    void sendNotificationNewReservationDriving(String passengerEmail, Driving driving);

    void sendNotificationNewDriving(String passengerEmail, Driving driving);

    void sendNotificationRejectDriving(List<String> passengersToSendNotifications, String startLocation, String endLocation);

    void sendNotificationRejectDrivingFromDriverToAdmin(String adminEmail, Driving driving, String driverEmail, String reason);

    void sendNotificationForPaymentExpired(List<String> passengersToSendNotifications);

    void sendNotificationForPaymentFailure(List<String> passengersToSendNotifications);

    void sendNotificationForPaymentSuccess(List<String> passengersToSendNotifications);

    void sendNotificationCancelDriving(String passengerEmail, Driving driving);

    void sendNotificationNewDrivingDriver(String driverEmail);

    void sendNotificationDriverArrivedAtStartLocation(Collection<String> passengersToSendNotifications);

    void reportDriverNotification(Passenger initiator);

    void sendNotificationRejectDrivingFromDriverToPassengers(List<String> passengersToSendNotifications);

    List<NotificationDTO> findAll();

    void deleteNotification(Long id);

    void deleteNotificationFromAdmin(Long drivingId);

    void sendNotificationReservationReminder(Integer startMinutes, List<User> userForNotification);

    void sendNotificationForPaymentReservation(Driving d);

    void sendNotificationForReservationDeleted(Driving d, String message);

    void sendNotificationToAdminForDriverChangeData(UserDTO driverDTO);

    void sendNotificationToAdminForCarChangeData(CarDTO carDTO);

    void sendNotificationAdminResponseForChanges(String driverEmail, String response);
}
