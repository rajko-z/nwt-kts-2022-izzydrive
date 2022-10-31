package com.izzydrive.backend.controller;

import com.izzydrive.backend.service.DrivingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/drivings")
@AllArgsConstructor
public class DrivingController {

    private final DrivingService drivingService;
}
