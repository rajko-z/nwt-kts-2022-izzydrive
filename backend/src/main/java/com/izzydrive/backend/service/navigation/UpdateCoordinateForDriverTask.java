package com.izzydrive.backend.service.navigation;

import com.izzydrive.backend.config.SpringContext;
import com.izzydrive.backend.service.users.DriverService;
import com.izzydrive.backend.service.users.impl.DriverServiceImpl;

public class UpdateCoordinateForDriverTask implements Runnable{

    private final String driverEmail;
    private final double lat;
    private final double lon;

    private DriverService getDriverService() {
        return SpringContext.getBean(DriverServiceImpl.class);
    }

    public UpdateCoordinateForDriverTask(String driverEmail, double lat, double lon) {
        this.driverEmail = driverEmail;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public void run() {
        getDriverService().updateCoordinatesForDriver(driverEmail, lat, lon);
    }
}
