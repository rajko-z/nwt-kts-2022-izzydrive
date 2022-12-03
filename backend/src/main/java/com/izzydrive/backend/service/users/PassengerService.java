package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.exception.*;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface PassengerService {

    public void registerPassenger(NewPassengerDTO newPassengerData) throws InvalidLastNameFormatException, InvalidPasswordFormatException, InvalidRepeatedPasswordException, InvalidPhoneNumberFormatException, InvalidEmailFormatException, MessagingException, TemplateException, IOException;

}