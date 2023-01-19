package com.izzydrive.backend.service.navigation;

import com.izzydrive.backend.config.SpringContext;
import com.izzydrive.backend.model.Location;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.List;

public class NavigationTask implements Runnable{

    private final String driverEmail;
    private final List<Location> locations;
    private final double totalDurationInSeconds;

    private ThreadPoolTaskScheduler getThreadPoolTaskScheduler() {
        return SpringContext.getBean(ThreadPoolTaskScheduler.class);
    }

    public NavigationTask(String driverEmail,
                          List<Location> locations,
                          double totalDurationInSeconds) {
        this.driverEmail = driverEmail;
        this.locations = locations;
        this.totalDurationInSeconds = totalDurationInSeconds;
    }

    @Override
    public void run() {
        if (this.locations.isEmpty()) {
            return;
        }

        int interval = (int)this.totalDurationInSeconds / this.locations.size();
        long intervalInMillis = interval * 1000L;

        int counter = 0;
        for (Location l : this.locations) {

            UpdateCoordinateForDriverTask job = new UpdateCoordinateForDriverTask(
                    driverEmail,
                    l.getLatitude(),
                    l.getLongitude());

            getThreadPoolTaskScheduler().schedule(
                    job,
                    new Date(System.currentTimeMillis() + counter*intervalInMillis)
            );
            counter++;
        }
    }
}
