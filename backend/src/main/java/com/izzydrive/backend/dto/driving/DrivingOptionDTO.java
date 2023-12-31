package com.izzydrive.backend.dto.driving;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivingOptionDTO {
    private DriverDTO driver;
    private LocationDTO driverCurrentLocation;
    private int timeForWaiting; // in minutes
    private double price;
    private CalculatedRouteDTO driverToStartPath;
    private CalculatedRouteDTO startToEndPath;
    private Boolean reservation;

    public DrivingOptionDTO(DriverDTO driver, LocationDTO driverCurrentLocation,
                            double price, CalculatedRouteDTO startToEndPath,
                            CalculatedRouteDTO driverToStartPath, Boolean reservation) {
        this.driver = driver;
        this.driverCurrentLocation = driverCurrentLocation;
        this.price = price;
        this.startToEndPath = startToEndPath;
        this.driverToStartPath = driverToStartPath;
        this.reservation = reservation;
    }
}
