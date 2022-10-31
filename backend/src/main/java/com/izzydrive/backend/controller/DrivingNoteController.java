package com.izzydrive.backend.controller;

import com.izzydrive.backend.service.DrivingNoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/driving-notes")
@AllArgsConstructor
public class DrivingNoteController {

    private final DrivingNoteService drivingNoteService;
}
