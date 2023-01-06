package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.AddressOnMapDTO;
import com.izzydrive.backend.dto.DrivingOptionDTO;
import com.izzydrive.backend.service.impl.DrivingFinderServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/driving-finder")
@AllArgsConstructor
public class DrivingFinderController {

    private final DrivingFinderServiceImpl drivingFinderService;

    @GetMapping("/simple/{latP1}/{lonP1}/{latP2}/{lonP2}")
    public ResponseEntity<List<DrivingOptionDTO>> findSimpleDrivings(@PathVariable double latP1,
                                                                     @PathVariable double lonP1,
                                                                     @PathVariable double latP2,
                                                                     @PathVariable double lonP2) {
        List<DrivingOptionDTO> retVal = drivingFinderService.getSimpleDrivingOptions(new AddressOnMapDTO(lonP1, latP1), new AddressOnMapDTO(lonP2, latP2));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }


}
