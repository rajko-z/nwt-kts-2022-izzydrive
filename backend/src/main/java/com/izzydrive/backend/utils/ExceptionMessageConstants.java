package com.izzydrive.backend.utils;

public final class ExceptionMessageConstants {

    public static final String INVALID_REPEATED_PASSWORD_MESSAGE = "Password and repeated password does not match";
    public static final String INVALID_PASSWORD_FORMAT_MESSAGE = "Password length must be minimum 8 characters";
    public static final String INVALID_EMAIl_FORMAT_MESSAGE = "Invalid Email format. Email must be 'someone@example.com'";
    public static final String INVALID_NAME_FORMAT_MESSAGE = "Invalid name format. Name only contains string and spaces";
    public static final String INVALID_PHONE_NUMBER_FORMAT_MESSAGE = "Invalid phone number format. Allowed format is '+123456789000'" ;
    public static final String SOMETHING_WENT_WRONG_MESSAGE = "Something went wrong, please try again." ;
    public static final String MAIL_ERROR_MESSAGE = "There was a problem with sending the email, please try again" ;
    public static final String USER_ALREADY_EXISTS_MESSAGE = "User with the same email already exits." ;
    public static final String AlREADY_SEND_REGISTRATION_REQUEST_MESSAGE = "You already send registration request. Look at your email box.";
    public static final String NEW_PASSWORD_SAME_AS_PREVIOUS = "Password can't be same sa previous";
    public static final String INVALID_CURRENT_PASSWORD = "Entered current password is incorrect";
    public static final String INVALID_LOGIN = "Invalid username or password";

    public static String userWithEmailDoesNotExist(String email) {
        return String.format("User with email: %s does not exists", email);
    }
}
