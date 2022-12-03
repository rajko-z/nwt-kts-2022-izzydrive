package com.izzydrive.backend.dto;

public class NewPassengerDTO {
    private String email;
    private String password;
    private String repeatedPassword;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public NewPassengerDTO(){

    }

    public NewPassengerDTO(String email, String password, String repeatedPassword, String firstName, String lastName, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.repeatedPassword = repeatedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
