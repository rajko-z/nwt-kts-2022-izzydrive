package com.izzydrive.backend.utils;

import com.izzydrive.backend.exception.*;
import com.izzydrive.backend.model.car.CarType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.izzydrive.backend.utils.ExceptionMessageConstants.*;

public class Validator {

    private Validator() { }

    public static boolean validateEmail(String email) {
        String regex = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern emailPattern = Pattern.compile(regex);
        Matcher matcherEmail = emailPattern.matcher(email);

        if(matcherEmail.matches()) return true;
        else {
            throw new BadRequestException(INVALID_EMAIl_FORMAT_MESSAGE, 1001);
        }

    }

    public static boolean validateLastName(String name){
        String regex = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        if(matcher.matches()) return true;
        else{
            throw new BadRequestException(INVALID_NAME_FORMAT_MESSAGE, 1002);
        }
    }

    public static boolean validateFirstName(String name){
        String regex = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        if(matcher.matches()) return true;
        else{
            throw new BadRequestException(INVALID_NAME_FORMAT_MESSAGE, 1007);
        }
    }


    public static boolean validatePhoneNumber(String phoneNumber){
        String regex = "^[+][0-9]{11,12}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if(matcher.matches()) return true;
        else{
            throw new BadRequestException(INVALID_PHONE_NUMBER_FORMAT_MESSAGE, 1005);
        }
    }

    public static boolean validateMatchingPassword(String password, String repeatedPassword){
        validatePassword(password);

        if(!password.equals(repeatedPassword)){
            throw new BadRequestException(INVALID_REPEATED_PASSWORD_MESSAGE, 1004);
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
            throw new BadRequestException(INVALID_CAR_REGISTRATION_MESSAGE, 1008);
        }
    }

    public static boolean validateCarType(String carType){
        for (CarType type : CarType.values()) {
            if (type.name().equalsIgnoreCase(carType)) {
                return true;
            }
        }
        throw new BadRequestException(INVALID_CAR_TYPE_MESSAGE);
    }

    public static boolean validatePassword(String password) {
        if(password.length() < 8){
            throw new BadRequestException(INVALID_PASSWORD_FORMAT_MESSAGE, 1003);
        }
        return true;
    }

}
