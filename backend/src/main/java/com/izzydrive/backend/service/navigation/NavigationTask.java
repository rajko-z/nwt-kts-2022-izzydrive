package com.izzydrive.backend.service.navigation;

import com.izzydrive.backend.config.SpringContext;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.Location;
import com.izzydrive.backend.service.navigation.tasks.DriverArrivedAtStartLocationTask;
import com.izzydrive.backend.service.navigation.tasks.UpdateCoordinateForDriverTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.List;

public class NavigationTask implements Runnable{

    private final Driving driving;

    private final List<Location> locations;

    private final boolean navigationFromDriverToStart;

    private ThreadPoolTaskScheduler getThreadPoolTaskScheduler() {
        return SpringContext.getBean(ThreadPoolTaskScheduler.class);
    }

    public NavigationTask(Driving driving, List<Location> locations, boolean navigationFromDriverToStart) {
        this.driving = driving;
        this.locations = locations;
        this.navigationFromDriverToStart = navigationFromDriverToStart;
    }

    @Override
    public void run() {
        double totalDuration = navigationFromDriverToStart ?
                driving.getDurationFromDriverToStart() : driving.getDuration();

        if (locations.isEmpty()) {
            return;
        }

        int interval = (int)totalDuration / locations.size();
        long intervalInMillis = interval * 1000L;

        int counter = 0;
        for (Location l : locations) {
            UpdateCoordinateForDriverTask job = new UpdateCoordinateForDriverTask(
                    driving.getDriver().getEmail(),
                    l.getLatitude(),
                    l.getLongitude());

            getThreadPoolTaskScheduler().schedule(
                    job,
                    new Date(System.currentTimeMillis() + counter*intervalInMillis)
            );
            counter++;
        }

        if (navigationFromDriverToStart) {
            getThreadPoolTaskScheduler().schedule(
                    new DriverArrivedAtStartLocationTask(driving),
                    new Date(System.currentTimeMillis() + counter*intervalInMillis)
            );
        }
    }
}
