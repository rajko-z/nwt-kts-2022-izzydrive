package com.izzydrive.backend.exception;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.izzydrive.backend.utils.ExceptionMessageConstants;
import freemarker.template.TemplateException;
import org.javatuples.Pair;

import javax.mail.MessagingException;

import static java.util.Map.entry;

public class CustomExceptionCode {

    public static Map<Object, Pair<Integer, String>> exceptions =  Map.ofEntries(
            entry(InvalidEmailFormatException.class, new Pair<>(1001, ExceptionMessageConstants.INVALID_EMAIl_FORMAT_MESSAGE)),
            entry(InvalidLastNameFormatException.class, new Pair<>(1002, ExceptionMessageConstants.INVALID_NAME_FORMAT_MESSAGE)),
            entry(InvalidPasswordFormatException.class, new Pair<>(1003, ExceptionMessageConstants.INVALID_PASSWORD_FORMAT_MESSAGE)),
            entry(InvalidRepeatedPasswordException.class, new Pair<>(1004, ExceptionMessageConstants.INVALID_REPEATED_PASSWORD_MESSAGE)),
            entry(InvalidPhoneNumberFormatException.class, new Pair<>(1005, ExceptionMessageConstants.INVALID_PHONE_NUMBER_FORMAT_MESSAGE)),
            entry(RuntimeException.class, new Pair<>(1006, ExceptionMessageConstants.SOMETHING_WENT_WRONG_MESSAGE)),
            entry(InvalidFirstNameFormatException.class, new Pair<>(1007, ExceptionMessageConstants.INVALID_NAME_FORMAT_MESSAGE)),
            entry(MessagingException.class, new Pair<>(1006, ExceptionMessageConstants.MAIL_ERROR_MESSAGE)),
            entry(TemplateException.class, new Pair<>(1006, ExceptionMessageConstants.MAIL_ERROR_MESSAGE)),
            entry(IOException.class, new Pair<>(1006, ExceptionMessageConstants.SOMETHING_WENT_WRONG_MESSAGE)),
            entry(UserAlreadyExistsException.class, new Pair<>(1006, ExceptionMessageConstants.USER_ALREADY_EXISTS_MESSAGE)),
            entry(AlreadySendRegistrationRequestException.class, new Pair<>(1006, ExceptionMessageConstants.AlREADY_SEND_REGISTRATION_REQUEST_MESSAGE)),
            entry(InvalidCarRegistrationException.class, new Pair<>(1008, ExceptionMessageConstants.INVALID_CAR_REGISTRATION_MESSAGE)),
            entry(AlreadyExitingCarException.class, new Pair<>(1008, ExceptionMessageConstants.ALREADY_EXISTING_CAR_MESSAGE))

    );

    public static ErrorMessage getErrorMessageFromException(Exception exception){
        Pair<Integer, String> code_and_message = CustomExceptionCode.exceptions.get(exception.getClass());
        return new ErrorMessage(
                (Integer) code_and_message.getValue(0),
                new Date(),
                (String) code_and_message.getValue(1)
        );
    }
}


