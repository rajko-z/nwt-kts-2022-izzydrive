package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.model.car.Car;

public interface CarService {

    Car createNewCar(CarDTO carData);
    void saveCar(Car car);
}
