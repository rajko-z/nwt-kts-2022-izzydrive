package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/test")
@AllArgsConstructor
public class TestUtilController {

    private final DrivingService drivingService;

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/set-driving-creation-time/{drivingId}")
    public ResponseEntity<TextResponse> setCreationDateForDriving(@PathVariable Long drivingId) {
        Driving driving = drivingService.findById(drivingId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST));

        driving.setCreationDate(LocalDateTime.now());
        drivingService.saveAndFlush(driving);
        return new ResponseEntity<>(new TextResponse("Success"), HttpStatus.OK);
    }
}
