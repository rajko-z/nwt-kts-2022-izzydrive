package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.DriverLocationDTO;
import com.izzydrive.backend.service.users.driver.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/drivers")
@AllArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping("add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> addNewDriver(@RequestBody DriverDTO driverDTO){
        driverService.addNewDriver(driverDTO);
        return ResponseEntity.ok("");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllDrivers() {
        List<UserDTO> drivers = driverService.findAllDrivers();
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }

    @GetMapping("/current-locations")
    public ResponseEntity<List<DriverLocationDTO>> findAllActiveDriversLocation() {
        List<DriverLocationDTO> locations = driverService.findAllActiveDriversLocation();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping("current-driving")
    public ResponseEntity<DrivingDTOWithLocations> getCurrentDriving(){
        DrivingDTOWithLocations driving = driverService.getCurrentDriving();
        return new ResponseEntity<>(driving, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping("next-driving")
    public ResponseEntity<DrivingDTOWithLocations> getNextDriving(){
        DrivingDTOWithLocations driving = driverService.getNextDriving();
        return new ResponseEntity<>(driving, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping("reservation")
    public ResponseEntity<DrivingDTOWithLocations> getReservation(){
        DrivingDTOWithLocations driving = driverService.getReservation();
        return new ResponseEntity<>(driving, HttpStatus.OK);
    }

}
