package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.service.DrivingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/drivings")
@AllArgsConstructor
public class DrivingController {

    private final DrivingService drivingService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("driver/{driverId}")
    public ResponseEntity<List<DrivingDTO>> findAllByDriverId(@PathVariable Long driverId){
        List<DrivingDTO> drivings = drivingService.findAllByDriverId(driverId);
        return new ResponseEntity<>(drivings, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("passenger/{passengerId}")
    public ResponseEntity<List<DrivingDTO>> findAllByPassengerId(@PathVariable Long passengerId){
        List<DrivingDTO> drivings = drivingService.findAllByPassengerId(passengerId);
        return new ResponseEntity<>(drivings, HttpStatus.OK);
    }
}
