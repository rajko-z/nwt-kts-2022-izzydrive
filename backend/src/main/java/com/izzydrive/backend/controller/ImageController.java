package com.izzydrive.backend.controller;

import com.izzydrive.backend.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/images")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;
}
