package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.dto.AdminResponseOnChanges;
import com.izzydrive.backend.service.NotificationService;
import com.izzydrive.backend.service.users.admin.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admins")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    private final NotificationService notificationService;

    @PostMapping("/response-changes")
    public void responseDriverChanges(@RequestBody AdminResponseOnChanges response){
        this.notificationService.sendNotificationAdminResponseForChanges(response.getDriverEmail(), response.getResponse());
    }
}
