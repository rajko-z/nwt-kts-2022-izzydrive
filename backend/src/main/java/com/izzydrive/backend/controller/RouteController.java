package com.izzydrive.backend.controller;

import com.izzydrive.backend.service.RouteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/routes")
@AllArgsConstructor
public class RouteController {

    private final RouteService routeService;
}
