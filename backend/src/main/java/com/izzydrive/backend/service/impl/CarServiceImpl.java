package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.car.Car;
import com.izzydrive.backend.model.car.CarAccommodation;
import com.izzydrive.backend.model.car.CarType;
import com.izzydrive.backend.repository.CarRepository;
import com.izzydrive.backend.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.izzydrive.backend.utils.ExceptionMessageConstants.ALREADY_EXISTING_CAR_MESSAGE;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    public Car createNewCar(CarDTO carData){
        Optional<Car> existingCar = carRepository.findByRegistration(carData.getRegistration());
        if(existingCar.isEmpty()){
            Car car = new Car(carData.getRegistration(),
                    carData.getModel(),
                    carData.getMaxPassengers(),
                    getCarType(carData.getCarType()),
                    getCarAccommodation(carData.getCarAccommodation()));

            return carRepository.save(car);
        }
       else{
           throw new BadRequestException(ALREADY_EXISTING_CAR_MESSAGE, 1008);
        }

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

    private String getCarAccommodation(CarAccommodation accommodation){
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
