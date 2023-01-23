package com.izzydrive.backend.service.navigation;

import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.Location;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executor;

@Service
@AllArgsConstructor
public class NavigationServiceImpl implements NavigationService {

    private final Executor threadPoolExecutor;

    @Override
    public void startNavigationForDriver(Driving driving, List<Location> locations, boolean fromDriverToStart) {
        NavigationTask job = new NavigationTask(driving, locations, fromDriverToStart);
        threadPoolExecutor.execute(job);
    }
}
