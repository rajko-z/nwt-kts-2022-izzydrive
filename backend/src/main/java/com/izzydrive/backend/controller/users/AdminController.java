package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.service.users.admin.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admins")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;
}
