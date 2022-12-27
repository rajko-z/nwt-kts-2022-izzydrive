package com.izzydrive.backend.controller;

import com.izzydrive.backend.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/messages")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;
}
