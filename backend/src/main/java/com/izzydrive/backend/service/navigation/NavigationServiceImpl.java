package com.izzydrive.backend.service.navigation;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Service
@AllArgsConstructor
public class NavigationServiceImpl implements NavigationService {

    private final Executor threadPoolExecutor;

    @Override
    public void startNavigationForDriver(DrivingDTOWithLocations driving, boolean fromDriverToStart) {
        NavigationTask job = new NavigationTask(driving, fromDriverToStart);
        threadPoolExecutor.execute(job);
    }
}
