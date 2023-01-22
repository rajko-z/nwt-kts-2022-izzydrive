package com.izzydrive.backend.controller;

import com.izzydrive.backend.service.users.driver.car.CarService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cars")
@AllArgsConstructor
public class CarController {

    private final CarService carService;
}
