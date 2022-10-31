package com.izzydrive.backend.controller;

import com.izzydrive.backend.service.AdminNoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin-notes")
@AllArgsConstructor
public class AdminNoteController {

    private final AdminNoteService adminNoteService;
}
