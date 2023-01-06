package com.izzydrive.backend.converters;

import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.service.CarService;

public class DriverDTOConverter {

    private DriverDTOConverter() {}

    public static DriverDTO convertBasicWithCar(Driver driver, CarService carService) {
        return DriverDTO.builder()
                .email(driver.getEmail())
                .phoneNumber(driver.getPhoneNumber())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .carData(CarDTO.builder()
                            .carType(driver.getCar().getCarType().name())
                            .carAccommodation(carService.getCarAccommodationFromString(driver.getCar().getCarAccommodations()))
                            .maxPassengers(driver.getCar().getMaxNumOfPassengers())
                            .registration(driver.getCar().getRegistration())
                            .model(driver.getCar().getModel())
                            .build())
                .build();
    }
}
