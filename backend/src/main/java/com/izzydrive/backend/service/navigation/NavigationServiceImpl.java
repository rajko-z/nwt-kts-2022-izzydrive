package com.izzydrive.backend.service.navigation;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class NavigationServiceImpl implements NavigationService {

    private static final Logger LOG = LoggerFactory.getLogger(NavigationServiceImpl.class);

    private final ThreadPoolTaskExecutor threadPoolExecutor;

    private static final Map<String, NavigationTask> runningNavigationTasks = new ConcurrentHashMap<>();

    @Override
    public void startNavigationForDriver(DrivingDTOWithLocations driving, boolean fromDriverToStart) {
        NavigationTask job = new NavigationTask(driving, fromDriverToStart);
        threadPoolExecutor.execute(job);
        runningNavigationTasks.put(driving.getDriver().getEmail(), job);
    }

    @Override
    public void stopNavigationForDriver(String email) {
        NavigationTask navigationTask =  runningNavigationTasks.get(email);
        navigationTask.stop();
    }
}
