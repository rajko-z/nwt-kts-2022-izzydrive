package com.izzydrive.backend.controller;

import com.izzydrive.backend.service.users.driver.workingtime.WorkingIntervalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/working-intervals")
@AllArgsConstructor
public class WorkingIntervalController {

    private final WorkingIntervalService workingIntervalService;

    @GetMapping("/get-minutes/{driverEmail}")
    public ResponseEntity<Long> getNumberOfMinutesDriverHasWorkedInLast24Hours(@PathVariable String driverEmail) {
        Long numberOfMinutes = workingIntervalService.getNumberOfMinutesDriverHasWorkedInLast24Hours(driverEmail);
        return new ResponseEntity<>(numberOfMinutes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PutMapping("/set-driver-active")
    public ResponseEntity<String> setDriverStatusToActive() {
        workingIntervalService.setCurrentLoggedDriverStatusToActive();
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PutMapping("/set-driver-inactive")
    public ResponseEntity<String> setDriverStatusToInActive() {
        workingIntervalService.setCurrentLoggedDriverStatusToInActive();
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
