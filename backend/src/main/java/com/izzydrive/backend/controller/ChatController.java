package com.izzydrive.backend.controller;


import com.izzydrive.backend.model.Message;
import com.izzydrive.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Sends a message to its destination channel
     *
     * @param message
     */
    @MessageMapping("/messages")
    public void handleMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        template.convertAndSend("/channel/chat/" + message.getChannel(), message);
    }
}
