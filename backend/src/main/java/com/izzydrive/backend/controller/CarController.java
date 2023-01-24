package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.service.users.driver.car.CarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN')")
    @PutMapping("/edit")
    public ResponseEntity<TextResponse> editCar(@RequestBody CarDTO carDTO, @RequestParam(defaultValue = "true") boolean saveChanges){
        boolean edited = this.carService.editCar(carDTO,saveChanges);
        if (edited){
            return new ResponseEntity<>(new TextResponse("Successfully changed car data"), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new TextResponse("Admin will verify your changes"), HttpStatus.OK);
        }
    }
}
