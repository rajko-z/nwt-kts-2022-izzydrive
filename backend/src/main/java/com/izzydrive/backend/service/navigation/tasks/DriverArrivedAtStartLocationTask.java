package com.izzydrive.backend.service.navigation.tasks;

import com.izzydrive.backend.config.SpringContext;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.notification.NotificationServiceImpl;
import com.izzydrive.backend.service.notification.driver.DriverNotificationService;
import com.izzydrive.backend.service.notification.driver.DriverNotificationServiceImpl;

public class DriverArrivedAtStartLocationTask  implements Runnable {

    private final DrivingDTOWithLocations driving;

    private NotificationService getNotificationService() {
        return SpringContext.getBean(NotificationServiceImpl.class);
    }

    private DriverNotificationService getDriverNotificationService() {
        return SpringContext.getBean(DriverNotificationServiceImpl.class);
    }

    public DriverArrivedAtStartLocationTask(DrivingDTOWithLocations driving) {
        this.driving = driving;
    }

    @Override
    public void run() {
        getNotificationService().sendNotificationDriverArrivedAtStartLocation(driving.getPassengers());
        getDriverNotificationService().sendSignalThatDriverArrivedAtStart(driving.getDriver().getEmail());
    }
}
