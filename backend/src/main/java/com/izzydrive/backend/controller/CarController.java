package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.service.users.driver.car.CarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cars")
@AllArgsConstructor
public class CarController {

    private final CarService carService;

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping
    public ResponseEntity<CarDTO> getDriverById(@RequestParam Long id) {
        CarDTO car = carService.findByDriverid(id);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }
}
