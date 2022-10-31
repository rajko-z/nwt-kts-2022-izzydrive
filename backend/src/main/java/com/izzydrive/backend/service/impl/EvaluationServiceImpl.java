package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.repository.EvaluationRepository;
import com.izzydrive.backend.service.EvaluationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;
}
