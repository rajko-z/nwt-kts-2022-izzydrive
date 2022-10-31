package com.izzydrive.backend.controller;

import com.izzydrive.backend.service.WorkingIntervalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/working-intervals")
@AllArgsConstructor
public class WorkingIntervalController {

    private final WorkingIntervalService workingIntervalService;
}
