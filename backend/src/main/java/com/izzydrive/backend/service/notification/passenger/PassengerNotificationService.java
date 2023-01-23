package com.izzydrive.backend.service.notification.passenger;

import java.util.List;

public interface PassengerNotificationService {
    void sendSignalThatRideHasStart(List<String> passengerEmails);
}
