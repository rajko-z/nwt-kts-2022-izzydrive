package com.izzydrive.backend.service;

import com.izzydrive.backend.model.Driving;

public interface NotificationService {

    void sendNotificationNewReservationDriving(String passengerEmail, Driving driving);

    void sendNotificationNewDriving(String passengerEmail, Driving driving);

    void sendNotificationRejectDriving(String passengerEmail, String startLocation, String endLocation);
}
