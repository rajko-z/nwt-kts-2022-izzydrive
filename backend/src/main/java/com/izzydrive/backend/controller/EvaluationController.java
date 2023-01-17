package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.EvaluationDTO;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.service.EvaluationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/evaluations")
@AllArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PostMapping("/add")
    public ResponseEntity<TextResponse> addEvaluation(@RequestBody EvaluationDTO evaluationDTO){
        this.evaluationService.addEvaluation(evaluationDTO);
        return new ResponseEntity<>(new TextResponse("Success"), HttpStatus.OK);
    }
}

