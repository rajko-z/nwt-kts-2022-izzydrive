package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.service.users.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@AllArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;
}
