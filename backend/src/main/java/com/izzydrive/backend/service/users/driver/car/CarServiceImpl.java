package com.izzydrive.backend.service.users.driver.car;

import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.car.*;
import com.izzydrive.backend.repository.CarRepository;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
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
                    getCarAccommodationString(carData.getCarAccommodation()));

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

    @Override
    public CarAccommodation getCarAccommodationFromString(String accommodation) {
        String[] tokens = accommodation.split(";");
        CarAccommodation carAccommodation = new CarAccommodation();

        for (String token : tokens) {
            if (token.equals(CarAccommodationEnum.BABY.name())) {
                carAccommodation.setBaby(true);
            } else if (token.equals(CarAccommodationEnum.PET.name())) {
                carAccommodation.setPet(true);
            } else if (token.equals(CarAccommodationEnum.BAGGAGE.name())) {
                carAccommodation.setBaggage(true);
            } else if (token.equals(CarAccommodationEnum.FOOD.name())) {
                carAccommodation.setFood(true);
            }
        }
        return carAccommodation;
    }

    @Override
    public double calculatePrice(Car car, double distance) {
        int km = (int)distance / 1000;
        if (km == 0) {
            km = 1;
        }
        switch (car.getCarType()) {
            case REGULAR:
                return 120 * km * CarPrices.REGULAR;
            case AVERAGE:
                return 120 * km * CarPrices.AVERAGE;
            case PREMIUM:
                return 120 * km * CarPrices.PREMIUM;
        }
        return 1;
    }

    @Override
    public CarDTO findByDriverid(Long id) {
        Car car = this.carRepository.findByUserId(id).orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.NO_CAR_FOR_USER));
        CarDTO carDTO =  new CarDTO(car);
        carDTO.setCarAccommodation(this.getCarAccommodationFromString(car.getCarAccommodations()));
        return carDTO;
    }

    private CarType getCarType(String carType){
        for (CarType type : CarType.values()) {
            if (type.name().equalsIgnoreCase(carType)) {
                return type;
            }
        }
        return CarType.REGULAR;
    }

    private String getCarAccommodationString(CarAccommodation accommodation){
        StringBuilder sb = new StringBuilder();

        if(accommodation.isBaby()){
            sb.append(CarAccommodationEnum.BABY.name()).append(";");
        }
        if(accommodation.isPet()){
            sb.append(CarAccommodationEnum.PET.name()).append(";");
        }
        if(accommodation.isFood()){
            sb.append(CarAccommodationEnum.FOOD.name()).append(";");
        }
        if(accommodation.isBaggage()){
            sb.append(CarAccommodationEnum.BAGGAGE.name());
        }

        return sb.toString();
    }

}
