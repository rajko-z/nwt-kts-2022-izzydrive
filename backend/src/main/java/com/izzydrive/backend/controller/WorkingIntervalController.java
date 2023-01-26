package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.service.users.driver.workingtime.WorkingIntervalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/working-intervals")
@AllArgsConstructor
public class WorkingIntervalController {

    private final WorkingIntervalService workingIntervalService;

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping("/get-minutes")
    public ResponseEntity<TextResponse> getNumberOfMinutesDriverHasWorkedInLast24Hours() {
        Long numberOfMinutes = workingIntervalService.getNumberOfMinutesLoggedDriverHasWorkedInLast24Hours();
        return new ResponseEntity<>(new TextResponse(numberOfMinutes.toString()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PutMapping("/set-driver-active")
    public ResponseEntity<TextResponse> setDriverStatusToActive() {
        workingIntervalService.setCurrentLoggedDriverStatusToActive();
        return new ResponseEntity<>(new TextResponse("Successfully changed status to active"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PutMapping("/set-driver-inactive")
    public ResponseEntity<TextResponse> setDriverStatusToInActive() {
        workingIntervalService.setCurrentLoggedDriverStatusToInActive();
        return new ResponseEntity<>(new TextResponse("Successfully changed status to inactive"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping("/get-driver-status")
    public ResponseEntity<TextResponse> getDriverStatus() {
        String text = workingIntervalService.isLoggedDriverActive() ? "active" : "inactive";
        return new ResponseEntity<>(new TextResponse(text), HttpStatus.OK);
    }
}
