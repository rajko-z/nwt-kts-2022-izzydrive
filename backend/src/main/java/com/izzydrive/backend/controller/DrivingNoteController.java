package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.DrivingNoteDTO;
import com.izzydrive.backend.service.DrivingNoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/driving-notes")
@AllArgsConstructor
public class DrivingNoteController {

    private final DrivingNoteService drivingNoteService;

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PostMapping("reject")
    public ResponseEntity<String> rejectDriving(@RequestBody DrivingNoteDTO drivingNoteDTO) {
        drivingNoteService.rejectDriving(drivingNoteDTO);
        return new ResponseEntity<>("Successfully denied driving", HttpStatus.OK);
    }
}
