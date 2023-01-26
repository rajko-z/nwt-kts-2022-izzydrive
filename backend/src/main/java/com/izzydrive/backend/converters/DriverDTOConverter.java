package com.izzydrive.backend.converters;

import com.izzydrive.backend.config.SpringContext;
import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.service.users.driver.car.CarService;
import com.izzydrive.backend.service.users.driver.car.CarServiceImpl;

public class DriverDTOConverter {

    private static CarService getCarService() {
        return SpringContext.getBean(CarServiceImpl.class);
    }

    private DriverDTOConverter() {}

    public static DriverDTO convertBasicWithCar(Driver driver) {
        return DriverDTO.builder()
                .email(driver.getEmail())
                .phoneNumber(driver.getPhoneNumber())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .driverStatus(driver.getDriverStatus())
                .location(new LocationDTO(driver.getLon(), driver.getLat()))
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
