package com.izzydrive.backend.controller;

import com.izzydrive.backend.model.Message;
import com.izzydrive.backend.model.ReadReceiptRequest;
import com.izzydrive.backend.repository.MessageRepository;
import com.izzydrive.backend.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/messages")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping(value = "/messages/{channelId}")
    public Page<Message> findMessages(Pageable pageable, @PathVariable("channelId") String channelId) {
        return messageService.findAllByChannel(channelId, pageable);
    }

    @PostMapping(value = "/messages")
    public void sendReadReceipt(@RequestBody ReadReceiptRequest request) {
        messageService.sendReadReceipt(request.getChannel(), request.getUsername());
    }
}
