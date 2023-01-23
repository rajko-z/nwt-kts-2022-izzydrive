package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notifications")
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PASSENGER', 'ROLE_DRIVER')")
    public ResponseEntity<List<NotificationDTO>> findAllNotifications() {
        List<NotificationDTO> notifications = notificationService.findAll();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PASSENGER', 'ROLE_DRIVER')")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id){
        notificationService.deleteNotification(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @DeleteMapping(value = "admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteNotificationFromAdmin(@PathVariable Long id){
        notificationService.deleteNotificationFromAdmin(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
