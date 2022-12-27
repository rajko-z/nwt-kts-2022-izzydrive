package com.izzydrive.backend.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.izzydrive.backend.service.notifications.FirebaseMessagingService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@Controller
@RequestMapping("/messages")
@AllArgsConstructor
public class NotificationsController {
    private final FirebaseMessagingService firebaseService;

    @Data
    public static class Note {
        private String subject;
        private String content;
        private Map<String, String> data;
        private String image;
    }

    @RequestMapping("/send-notification")
    @ResponseBody
    public String sendNotification(@RequestBody String token) throws FirebaseMessagingException {
        System.out.println(token);
        return firebaseService.sendNotification("customToken");
    }
}
