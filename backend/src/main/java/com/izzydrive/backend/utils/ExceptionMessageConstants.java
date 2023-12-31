package com.izzydrive.backend.utils;

public final class ExceptionMessageConstants {


    private ExceptionMessageConstants() {
    }


    public static final String INVALID_REPEATED_PASSWORD_MESSAGE = "Password and repeated password does not match";
    public static final String INVALID_PASSWORD_FORMAT_MESSAGE = "Password length must be minimum 8 characters";
    public static final String INVALID_EMAIL_FORMAT_MESSAGE = "Invalid Email format. Email must be 'someone@example.com'";
    public static final String INVALID_NAME_FORMAT_MESSAGE = "Invalid name format. Name only contains string and spaces";
    public static final String INVALID_PHONE_NUMBER_FORMAT_MESSAGE = "Invalid phone number format. Allowed format is '+123456789000'";
    public static final String SOMETHING_WENT_WRONG_MESSAGE = "Something went wrong, please try again.";
    public static final String MAIL_ERROR_MESSAGE = "There was a problem with sending the email, please try again";
    public static final String USER_ALREADY_EXISTS_MESSAGE = "User with the same email already exits.";
    public static final String ALREADY_SEND_REGISTRATION_REQUEST_MESSAGE = "You already send registration request. Look at your email box.";
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
    public static final String CANT_CHANGE_DS_TO_INACTIVE_CAUSE_DRIVINGS_EXISTS = "Can't change your status to inactive because there are still active drivings or drivings in waiting phase.";
    public static final String YOU_CAN_NOT_LINK_YOURSELF_FOR_DRIVE = "You can't link yourself for ride";
    public static final String INVALID_LOCATIONS_UNIQUENESS = "Provided locations don't have unique coordinates and names";
    public static final String ERROR_HAPPENED_WHILE_CALCULATING_ROUTES = "Error geocode while calculating routes";
    public static final String ERROR_SIZE_OF_INTERMEDIATE_LOCATIONS = "Currently only up to 3 intermediate locations are supported";
    public static final String DRIVER_NO_LONGER_AVAILABLE = "Sorry, but driver you chosen is no longer available. Please go back and choose another one, or try again later";
    public static final String YOU_ALREADY_HAVE_CURRENT_DRIVING = "You already have a ride, wait for the ride to finish and you can order another one";
    public static final String YOU_DO_NOT_HAVE_DRIVING_FOR_PAYMENT = "You do not have driving for payment";
    public static final String MAX_NUMBER_OF_LINKED_PASSENGERS = "You can add up to 3 linked passengers";
    public static final String ERROR_START_AND_END_LOCATION = "You have to provide one start and one end location";
    public static final String DRIVING_DOESNT_EXIST = "Driving with that id doesn't exists";
    public static final String ERROR_INVALID_ETH_ADDRESS = "ETH address is not valid";
    public static final String ERROR_INVALID_SECRET_KEY = "Secret key is not valid";
    public static final String ERROR_ETH_ADDRESS_DOES_NOT_MATCH_SECRET_KEY = "ETH address and private key does not match";
    public static final String INVALID_PERIOD_SCHEDULE_DRIVING = "The ride is scheduled at least 30 minutes before and at most 5 hours before";
    public static final String YOU_ALREADY_APPROVED_THIS_PAYING = "You already approved this paying";
    public static final String CANT_USE_EXISTING_PAYING_DATA = "Can't use existing paying data, because you dont have one";
    public static final String NOTIFICATION_DOESNT_EXIST = "Notification with that id doesn't exists";
    public static final String FAVORITE_ROUTES_NOT_FOUND = "You don't have favorite routes";
    public static final String YOU_DO_NOT_HAVE_CURRENT_WAITING_DRIVING = "You don't have current driving that you are waiting for";
    public static final String ALREADY_USED_THIS_LINK = "You already used this link";
    public static final String INVALID_RESET_PASSWORD_TOKEN = "There is no user with provided token";
    public static final String PASSENGER_NO_LONGER_AVAILABLE = "The passenger already has a ride!";
    public static final String NO_CAR_FOR_USER = "There is no car for provided user";
    public static final String YOU_DO_NOT_HAVE_CURRENT_WAITING_DRIVING_TO_START = "You don't have current waiting driving to start";
    public static final String CANT_START_DRIVING_NOT_AT_LOCATION = "You can't start driving because you didn't arrived at start location";
    public static final String CANT_REPORT_DRIVER_BECAUSE_NOT_ACTIVE_DRIVING = "You can't report driver because you do not have currently active driving";
    public static final String CANT_FINISH_DRIVING_THAT_NOT_ACTIVE = "You don't have active drivings to finish";
    public static final String CANT_FIND_DRIVING_TO_CANCEL = "Can't find driving to cancel";
    public static final String CANT_FIND_RESERVATION_TO_CANCEL = "Can't find reservation to cancel";
    public static final String INVALID_ROUTE_PROVIDED = "Invalid route provided. Chosen route can't be recalculated from request";
    public static final String PAYMENT_SESSION_EXPIRED = "Payment session expired";
    public static final String PAYMENT_FAILURE = "Payment failure, canceling current driving. Make sure every passenger input correct paying info and have enough funds.";


    public static String passengerAlreadyHasReservation(String email) {
        return String.format("The passenger %s already has a reservation in the selected period", email);
    }

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

    public static String userDoesntExist() {
        return "User doesn't exist";
    }

    public static String routeDoesntExist() {
        return "Route doesn't exist";
    }

}

