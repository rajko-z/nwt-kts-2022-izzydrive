package com.izzydrive.backend.service.navigation;

import com.izzydrive.backend.model.Location;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executor;

@Service
@AllArgsConstructor
public class NavigationServiceImpl {

    private final Executor threadPoolExecutor;

    public void startNavigationForDriver(String driverEmail, List<Location> locations, double totalDurationInSeconds) {
        NavigationTask job = new NavigationTask(
                driverEmail,
                locations,
                totalDurationInSeconds
        );
        threadPoolExecutor.execute(job);
    }
}
