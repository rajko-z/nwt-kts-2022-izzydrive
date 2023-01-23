package com.izzydrive.backend.service.navigation;

import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.Location;

import java.util.List;

public interface NavigationService {
    void startNavigationForDriver(Driving driving, List<Location> locations, boolean fromDriverToStart);
}
