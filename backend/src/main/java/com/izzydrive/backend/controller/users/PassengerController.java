package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/passengers")
@AllArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping("/registration")
    public ResponseEntity<Object> registerPassenger(@RequestBody NewPassengerDTO newPassengerData)  {
        passengerService.registerPassenger(newPassengerData);
        return ResponseEntity.ok("Success");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllDrivers() {
        List<UserDTO> drivers = passengerService.findAllPassenger();

        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/current-driving")
    public ResponseEntity<DrivingDTOWithLocations> findCurrentDrivingWithLocations() {
        DrivingDTOWithLocations retVal = this.passengerService.getCurrentDrivingForLoggedPassenger();
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/current-driving-left-to-start")
    public ResponseEntity<CalculatedRouteDTO> findEstimatedTimeLeftForCurrentDrivingToStart() {
        CalculatedRouteDTO retVal = passengerService.findEstimatedTimeLeftForCurrentDrivingToStart();
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/report-driver")
    public ResponseEntity<TextResponse> reportDriver() {
        passengerService.reportDriver();
        return new ResponseEntity<>(new TextResponse("Driver is successfully reported."), HttpStatus.OK);
    }
}
