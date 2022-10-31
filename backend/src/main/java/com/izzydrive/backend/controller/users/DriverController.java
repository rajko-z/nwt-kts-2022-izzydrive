package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.service.users.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/drivers")
@AllArgsConstructor
public class DriverController {

    private final DriverService driverService;
}
