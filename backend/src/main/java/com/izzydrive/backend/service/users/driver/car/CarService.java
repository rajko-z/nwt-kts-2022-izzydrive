package com.izzydrive.backend.service.users.driver.car;

import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.model.car.Car;
import com.izzydrive.backend.model.car.CarAccommodation;

public interface CarService {

    Car createNewCar(CarDTO carData);
    void saveCar(Car car);

    CarAccommodation getCarAccommodationFromString(String accommodation);

    double calculatePrice(Car car, double distance);
}
