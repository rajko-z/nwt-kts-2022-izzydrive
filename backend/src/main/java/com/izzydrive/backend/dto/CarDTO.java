package com.izzydrive.backend.dto;

import com.izzydrive.backend.model.car.CarAccommodation;

public class CarDTO {

    private String registration;
    private String model;
    private int maxPassengers;
    private String carType;
    private CarAccommodation carAccommodation;

    public CarDTO(String registration, String model, int maxPassengers, String carType, CarAccommodation carAccomodation) {
        this.registration = registration;
        this.model = model;
        this.maxPassengers = maxPassengers;
        this.carType = carType;
        this.carAccommodation = carAccomodation;
    }

    public CarDTO() {

    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setModel(String mode) {
        this.model = mode;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setCarAccommodation(CarAccommodation carAccommodation) {
        this.carAccommodation = carAccommodation;
    }

    public String getRegistration() {
        return registration;
    }

    public String getModel() {
        return model;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public String getCarType() {
        return carType;
    }

    public CarAccommodation getCarAccommodation() {
        return carAccommodation;
    }
}
