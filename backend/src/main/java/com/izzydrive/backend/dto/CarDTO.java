package com.izzydrive.backend.dto;

import com.izzydrive.backend.model.car.Car;
import com.izzydrive.backend.model.car.CarAccommodation;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CarDTO {

    public Long id;
    private String registration;
    private String model;
    private int maxPassengers;
    private String carType;
    private CarAccommodation carAccommodation;

    private String accommodations;

    private String driverEmal;

    public CarDTO(Car car){
        this.id = car.getId();
        this.registration = car.getRegistration();
        this.model = car.getModel();
        this.maxPassengers = car.getMaxNumOfPassengers();
        this.carType = car.getCarType().toString();
        this.accommodations = car.getCarAccommodations();
        this.driverEmal = car.getDriver().getEmail();
    }
}
