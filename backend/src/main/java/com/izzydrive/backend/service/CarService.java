package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.model.car.Car;

public interface CarService {

    public Car createNewCar(CarDTO carData);
    public void saveCar(Car car);
}
