package com.izzydrive.backend.service.navigation.tasks;

import com.izzydrive.backend.config.SpringContext;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.service.NotificationService;
import com.izzydrive.backend.service.impl.NotificationServiceImpl;
import com.izzydrive.backend.service.notification.DriverNotificationService;
import com.izzydrive.backend.service.notification.DriverNotificationServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

public class DriverArrivedAtStartLocationTask  implements Runnable {

    private final Driving driving;

    private NotificationService getNotificationService() {
        return SpringContext.getBean(NotificationServiceImpl.class);
    }

    private DriverNotificationService getDriverNotificationService() {
        return SpringContext.getBean(DriverNotificationServiceImpl.class);
    }

    public DriverArrivedAtStartLocationTask(Driving driving) {
        this.driving = driving;
    }

    @Override
    public void run() {
        List<String> passEmails = driving.getPassengers().stream().map(User::getEmail).collect(Collectors.toList());
        getNotificationService().sendNotificationDriverArrivedAtStartLocation(passEmails);
        getDriverNotificationService().sendSignalThatDriverArrivedAtStart(driving.getDriver().getEmail());
    }
}
