package com.izzydrive.backend.dto;

import com.izzydrive.backend.model.car.CarAccommodation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarDTO {
    private String registration;
    private String model;
    private int maxPassengers;
    private String carType;
    private CarAccommodation carAccommodation;
}
