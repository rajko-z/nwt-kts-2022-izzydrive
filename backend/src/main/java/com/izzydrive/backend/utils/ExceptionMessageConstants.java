package com.izzydrive.backend.utils;

public final class ExceptionMessageConstants {

    private ExceptionMessageConstants() {}
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
    public static final String INVALID_CAR_REGISTRATION_MESSAGE = "Invalid registration format. Required: XX-X*(3-5)-XX";
    public static final String ALREADY_EXISTING_CAR_MESSAGE = "Car with same registration already exists";
    public static final String USER_DOESNT_EXISTS = "User with that username doesn't exists";
    public static final String INVALID_VERIFICATION_TOKEN = "Invalid verification token!";
    public static final String EMAIL_HAS_ALREADY_BEEN_VERIFIED = "E-Mail has already been verified!";
    public static final String VERIFICATION_TOKEN_HAS_EXPIRED = "Your verification token has expired!";
    public static final String INVALID_CAR_TYPE_MESSAGE = "Invalid car type";
    public static final String USER_IS_BLOCK_MESSAGE = "The user has been blocked by the administrator!";
    public static final String LOCATION_OUTSIDE_OF_NOVI_SAD = "Currently only city of Novi Sad is supported";
    public static final String CANT_CHANGE_DS_TO_INACTIVE_CAUSE_DRIVINGS_EXISTS = "Can't change your status to inactive because there are still active drivings.";
    public static final String YOU_CAN_NOT_LINK_YOURSELF_FOR_DRIVE = "You can't link yourself for ride";
    public static final String INVALID_LOCATIONS_UNIQUENESS = "Provided locations don't have unique coordinates and names";
    public static final String ERROR_HAPPENED_WHILE_CALCULATING_ROUTES = "Error geocode while calculating routes";
    public static final String ERROR_SIZE_OF_INTERMEDIATE_LOCATIONS = "Currently only up to 3 intermediate locations are supported";
    public static final String DRIVER_NO_LONGER_AVAILABLE = "Sorry, but driver you chosen is no longer available. Please go back and choose another one, or try again later";
    public static final String YOU_ALREADY_HAVE_CURRENT_DRIVING = "You already have a ride, wait for the ride to finish and you can order another one";
    public static final String YOU_DON_NOT_HAVING_DRIVING_FOR_PAYMENT = "You do not have driving for payment";
    public static final String MAX_NUMBER_OF_LINKED_PASSENGERS = "You can add up to 3 linked passengers";
    public static final String ERROR_START_AND_END_LOCATION = "You have to provide one start and one end location";
    public static final String ERROR_INVALID_ETH_ADDRESS = "ETH address is not valid";
    public static final String ERROR_INVALID_SECRET_KEY = "Secret key is not valid";
    public static final String ERROR_ETH_ADDRESS_DOES_NOT_MATCH_SECRET_KEY = "ETH address and private key does not match";

    public static String userWithEmailDoesNotExist(String email) {
        return String.format("User with email: %s does not exists", email);
    }

    public static String cantLinkPassengerThatAlreadyHasCurrentDriving(String email) {
        return String.format("Can't link passenger with email: %s because he already has current driving or he is waiting for one", email);
    }

    public static String cantChangeDSToInactiveBecauseFutureDrivingIsStartingSoon(Long minutes) {
        return String.format("Can't change your status to inactive because you have reserved ride that starts in %s minutes", minutes);
    }

    public static String placeForGeoCodeDoesNotExist(double lon, double lat) {
        return String.format("Error geocode, can't locate place with lon:%s;lat:%s", lon, lat);
    }

    public static String cantLocatePlaceForText(String text) {
        return String.format("Error geocode, can't locate place for text: %s", text);
    }
}

