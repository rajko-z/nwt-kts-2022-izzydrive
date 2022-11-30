package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.exception.*;
import com.izzydrive.backend.service.users.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.izzydrive.backend.utils.ExceptionMessageConstants.SOMETHING_WENT_WRONG_MESSAGE;

@Controller
@RequestMapping("/passengers")
@AllArgsConstructor
public class PassengerController {

    @Autowired
    private final PassengerService passengerService;

    @PostMapping("/registration")
    public ResponseEntity<String> registerPassenger(@RequestBody NewPassengerDTO newPassengerData){
        try{
            passengerService.registerPassenger(newPassengerData);
            return ResponseEntity.ok("");
        }catch  (InvalidNameFormatException | InvalidPasswordFormatException | InvalidRepeatedPasswordException |
                InvalidPhoneNumberFormatException | InvalidEmailFormatException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(SOMETHING_WENT_WRONG_MESSAGE);
        }
    }

}
