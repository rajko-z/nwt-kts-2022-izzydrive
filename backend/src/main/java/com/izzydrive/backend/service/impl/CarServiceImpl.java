package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.model.Image;
import com.izzydrive.backend.model.car.Car;
import com.izzydrive.backend.model.car.CarAccommodation;
import com.izzydrive.backend.model.car.CarType;
import com.izzydrive.backend.repository.CarRepository;
import com.izzydrive.backend.repository.ImageRepository;
import com.izzydrive.backend.service.CarService;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    @Autowired
    private final CarRepository carRepository;

    public Car createNewCar(CarDTO carData){
        Car car = new Car(carData.getRegistration(),
                        carData.getModel(),
                        carData.getMaxPassengers(),
                        getCarType(carData.getCarType()),
                        getCarAccomodation(carData.getCarAccommodation()));

        return carRepository.save(car);

    }

    @Override
    public void saveCar(Car car) {
        carRepository.save(car);
    }

    private CarType getCarType(String carType){
        for (CarType type : CarType.values()) {
            if (type.name().equalsIgnoreCase(carType)) {
                return type;
            }
        }
        return CarType.REGULAR;
    }

    private String getCarAccomodation(CarAccommodation accommodation){
        String value = "";
        if(accommodation.isBaby()){
            value += "baby ";
        }
        if(accommodation.isPet()){
            value += "pet ";
        }
        if(accommodation.isFood()){
            value += "food ";
        }
        if(accommodation.isBaggage()){
            value += "bagge ";
        }

        return value;
    }

}
