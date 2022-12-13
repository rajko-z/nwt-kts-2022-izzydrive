package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.exception.*;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface PassengerService {

    void registerPassenger(NewPassengerDTO newPassengerData);

}