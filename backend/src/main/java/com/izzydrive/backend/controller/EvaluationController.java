package com.izzydrive.backend.controller;

import com.izzydrive.backend.service.EvaluationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/evaluations")
@AllArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;
}
