package com.izzydrive.backend.dto;

public class NewDriverDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private CarDTO carData;

    public NewDriverDTO(String firstName, String lastName, String email, String phoneNumber, CarDTO carData) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.carData = carData;
    }

    public NewDriverDTO(){

    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CarDTO getCarData() {
        return carData;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCarData(CarDTO carData) {
        this.carData = carData;
    }
}
