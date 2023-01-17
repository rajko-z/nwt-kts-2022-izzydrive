package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.DrivingFinderService;
import com.izzydrive.backend.service.DrivingService;
import com.izzydrive.backend.service.ProcessDrivingRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/drivings")
@AllArgsConstructor
public class DrivingController {

    private final DrivingService drivingService;

    private final DrivingFinderService drivingFinderService;

    private final ProcessDrivingRequestService processDrivingRequestService;

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

    @PostMapping("/finder/simple")
    public ResponseEntity<List<DrivingOptionDTO>> findSimpleDrivings(@RequestBody @Valid List<AddressOnMapDTO> addresses) {
        List<DrivingOptionDTO> retVal = drivingFinderService.getSimpleDrivingOptions(addresses);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PostMapping("finder/advanced")
    public ResponseEntity<List<DrivingOptionDTO>> findAdvancedDrivings(@RequestBody @Valid DrivingFinderRequestDTO request) {
        List<DrivingOptionDTO> retVal = drivingFinderService.getAdvancedDrivingOptions(request);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PostMapping(value = "/process")
    public ResponseEntity<TextResponse> processDrivingRequest(@RequestBody DrivingRequestDTO request) {
        this.processDrivingRequestService.process(request);
        return new ResponseEntity<>(new TextResponse("Success"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping(value = "/reject-linked-user/{drivingId}")
    public ResponseEntity<TextResponse> rejectDrivingLinkedUser(@PathVariable Long drivingId){
        this.drivingService.rejectDrivingLinkedUser(drivingId);
        return new ResponseEntity<>(new TextResponse("Successfully denied driving"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/{id}")
    public ResponseEntity<DrivingDTO> findDrivingById(@PathVariable Long id){
        DrivingDTO driving = drivingService.findById(id);
        return new ResponseEntity<>(driving, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PASSENGER')")
    @GetMapping("passenger/history/{passengerId}")
    public ResponseEntity<List<DrivingDTO>> getPassengerDrivingHistory(@PathVariable Long passengerId){
        List<DrivingDTO> drivings = drivingService.getPassengerDrivingHistory(passengerId);
        return new ResponseEntity<>(drivings, HttpStatus.OK);
    }
}
