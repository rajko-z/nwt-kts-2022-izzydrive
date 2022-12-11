package com.izzydrive.backend.service;

import com.izzydrive.backend.exception.*;
import com.izzydrive.backend.model.car.CarType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.izzydrive.backend.utils.ExceptionMessageConstants.*;

public class Validator {

    public static boolean validateEmail(String email) throws InvalidEmailFormatException {
        String regex = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern emailPattern = Pattern.compile(regex);
        Matcher matcherEmail = emailPattern.matcher(email);

        if(matcherEmail.matches()) return true;

        else {
            throw new InvalidEmailFormatException(INVALID_EMAIl_FORMAT_MESSAGE);
        }

    }

    public static boolean validateLastName(String name) throws InvalidLastNameFormatException {
        String regex = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        if(matcher.matches()) return true;
        else{
            throw new InvalidLastNameFormatException(INVALID_NAME_FORMAT_MESSAGE);
        }
    }

    public static boolean validateFirstName(String name) throws InvalidFirstNameFormatException {
        String regex = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        if(matcher.matches()) return true;
        else{
            throw new InvalidLastNameFormatException(INVALID_NAME_FORMAT_MESSAGE);
        }
    }


    public static boolean validatePhoneNumber(String phoneNumber) throws InvalidPhoneNumberFormatException {
//        "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$"
        String regex = "^[+][0-9]{12,12}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if(matcher.matches()) return true;
        else{
            throw new InvalidPhoneNumberFormatException(INVALID_PHONE_NUMBER_FORMAT_MESSAGE);
        }
    }

    public static boolean validateMatchingPassword(String password, String repeatedPassword)throws InvalidRepeatedPasswordException, InvalidPasswordFormatException {
        if(password.length() < 8){
            throw new InvalidPasswordFormatException(INVALID_PASSWORD_FORMAT_MESSAGE);
        }
        else if(!password.equals(repeatedPassword)){
            throw new InvalidRepeatedPasswordException(INVALID_REPEATED_PASSWORD_MESSAGE);
        }
        else{
            return true;
        }
    }

    public static boolean validateCarRegistration(String registration){
        String regex = "^[a-zA-Z]{2,2}[-][0-9]{3,5}[-][a-zA-Z]{2,2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(registration);
        if(matcher.matches()) return true;
        else{
            throw new InvalidCarRegistrationException(INVALID_CAR_REGISTRATION);
        }
    }

    public static boolean validateCarType(String carType){
        for (CarType type : CarType.values()) {
            if (type.name().equalsIgnoreCase(carType)) {
                return true;
            }
        }
        return false;
    }




}
