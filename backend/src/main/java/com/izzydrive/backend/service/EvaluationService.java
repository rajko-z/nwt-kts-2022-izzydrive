package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.EvaluationDTO;
import com.izzydrive.backend.model.Evaluation;

import java.util.List;

public interface EvaluationService {

    List<Evaluation> findAll();

    void addEvaluation(EvaluationDTO evalutionDTO);

    List<EvaluationDTO> findAllByDrivingId(Long drivingId);
}
