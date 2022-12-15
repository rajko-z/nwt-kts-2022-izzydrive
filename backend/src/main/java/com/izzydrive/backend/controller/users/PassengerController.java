package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.exception.*;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;

import static com.izzydrive.backend.utils.ExceptionMessageConstants.SOMETHING_WENT_WRONG_MESSAGE;

@Controller
@RequestMapping("/passengers")
@AllArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping("/registration")
    public ResponseEntity<Object> registerPassenger(@RequestBody NewPassengerDTO newPassengerData){
        try {
            passengerService.registerPassenger(newPassengerData);
            return ResponseEntity.ok("");
        } catch (MessagingException | TemplateException| IOException e) {
            return new ResponseEntity<>(CustomExceptionCode.getErrorMessageFromException(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
