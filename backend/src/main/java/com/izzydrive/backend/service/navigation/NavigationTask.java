package com.izzydrive.backend.service.navigation;

import com.izzydrive.backend.config.SpringContext;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.service.navigation.tasks.DriverArrivedAtStartLocationTask;
import com.izzydrive.backend.service.navigation.tasks.UpdateCoordinateForDriverTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class NavigationTask implements Runnable{

    private final Map<Integer, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    private final DrivingDTOWithLocations driving;

    private final boolean navigationFromDriverToStart;

    private ThreadPoolTaskScheduler getThreadPoolTaskScheduler() {
        return SpringContext.getBean(ThreadPoolTaskScheduler.class);
    }

    public NavigationTask(DrivingDTOWithLocations driving, boolean navigationFromDriverToStart) {
        this.driving = driving;
        this.navigationFromDriverToStart = navigationFromDriverToStart;
    }

    @Override
    public void run() {
        double totalDuration = driving.getFromDriverToStart().getDuration();
        List<LocationDTO> locations = driving.getFromDriverToStart().getCoordinates();
        if (!navigationFromDriverToStart) {
            totalDuration = driving.getFromStartToEnd().getDuration();
            locations = driving.getFromStartToEnd().getCoordinates();
        }

        if (locations.isEmpty()) {
            return;
        }

        int interval = (int)totalDuration / locations.size();
        long intervalInMillis = interval * 1000L;

        int counter = 0;
        for (LocationDTO l : locations) {
            UpdateCoordinateForDriverTask job = new UpdateCoordinateForDriverTask(
                    driving.getDriver().getEmail(),
                    l.getLat(),
                    l.getLon()
            );
            ScheduledFuture<?> futureTask = getThreadPoolTaskScheduler().schedule(
                    job,
                    new Date(System.currentTimeMillis() + counter*intervalInMillis)
            );
            this.scheduledTasks.put(counter, futureTask);
            counter++;
        }

        if (navigationFromDriverToStart) {
            ScheduledFuture<?> futureTask = getThreadPoolTaskScheduler().schedule(
                    new DriverArrivedAtStartLocationTask(driving),
                    new Date(System.currentTimeMillis() + counter*intervalInMillis)
            );
            this.scheduledTasks.put(counter, futureTask);
        }
    }

    public void stop() {
        for (Map.Entry<Integer, ScheduledFuture<?>> entry: this.scheduledTasks.entrySet()) {
            entry.getValue().cancel(true);
        }
    }
}
