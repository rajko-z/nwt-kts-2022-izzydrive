package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.service.DrivingFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/driving-finder")
@AllArgsConstructor
public class DrivingFinderController {

    private final DrivingFinderService drivingFinderService;

    @GetMapping("/simple/{latP1}/{lonP1}/{latP2}/{lonP2}")
    public ResponseEntity<List<DrivingOptionDTO>> findSimpleDrivings(@PathVariable double latP1,
                                                                     @PathVariable double lonP1,
                                                                     @PathVariable double latP2,
                                                                     @PathVariable double lonP2) {
        List<DrivingOptionDTO> retVal = drivingFinderService.getSimpleDrivingOptions(new AddressOnMapDTO(lonP1, latP1), new AddressOnMapDTO(lonP2, latP2));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PostMapping("/advanced")
    public ResponseEntity<List<DrivingOptionDTO>> findAdvancedDrivings(@RequestBody @Valid DrivingFinderRequestDTO request) {
        List<DrivingOptionDTO> retVal = drivingFinderService.getAdvancedDrivingOptions(request);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

}
