package com.izzydrive.backend.service.navigation.tasks;

import com.izzydrive.backend.config.SpringContext;
import com.izzydrive.backend.service.users.driver.location.DriverLocationService;
import com.izzydrive.backend.service.users.driver.location.DriverLocationServiceImpl;

public class UpdateCoordinateForDriverTask implements Runnable{

    private final String driverEmail;
    private final double lat;
    private final double lon;

    private DriverLocationService getDriverLocationService() {
        return SpringContext.getBean(DriverLocationServiceImpl.class);
    }

    public UpdateCoordinateForDriverTask(String driverEmail, double lat, double lon) {
        this.driverEmail = driverEmail;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public void run() {
        getDriverLocationService().updateCoordinatesForDriver(driverEmail, lat, lon);
    }
}
