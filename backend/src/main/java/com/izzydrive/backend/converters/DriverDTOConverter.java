package com.izzydrive.backend.converters;

import com.izzydrive.backend.config.SpringContext;
import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.service.users.driver.car.CarService;
import com.izzydrive.backend.service.users.driver.car.CarServiceImpl;
import com.izzydrive.backend.service.users.driver.location.DriverLocationService;
import com.izzydrive.backend.service.users.driver.location.DriverLocationServiceImpl;

public class DriverDTOConverter {

    private static CarService getCarService() {
        return SpringContext.getBean(CarServiceImpl.class);
    }

    private static DriverLocationService getDriverLocationService() { return SpringContext.getBean(DriverLocationServiceImpl.class);}

    private DriverDTOConverter() {}

    public static DriverDTO convertBasicWithCar(Driver driver) {
        return DriverDTO.builder()
                .email(driver.getEmail())
                .phoneNumber(driver.getPhoneNumber())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .driverStatus(driver.getDriverStatus())
                .location(getDriverLocationService().getDriverLocation(driver.getEmail()))
                .carData(CarDTO.builder()
                            .carType(driver.getCar().getCarType().name())
                            .carAccommodation(getCarService().getCarAccommodationFromString(driver.getCar().getCarAccommodations()))
                            .maxPassengers(driver.getCar().getMaxNumOfPassengers())
                            .registration(driver.getCar().getRegistration())
                            .model(driver.getCar().getModel())
                            .build())
                .build();
    }
}
