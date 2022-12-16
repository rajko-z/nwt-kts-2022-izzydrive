package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.service.users.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/passengers")
@AllArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping("/registration")
    public ResponseEntity<Object> registerPassenger(@RequestBody NewPassengerDTO newPassengerData)  {
        passengerService.registerPassenger(newPassengerData);
        return ResponseEntity.ok("");
    }

}
