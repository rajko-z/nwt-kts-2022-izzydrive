package com.izzydrive.backend.dto;

import com.izzydrive.backend.model.car.CarAccommodation;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CarDTO {
    private String registration;
    private String model;
    private int maxPassengers;
    private String carType;
    private CarAccommodation carAccommodation;
}
