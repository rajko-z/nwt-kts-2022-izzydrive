package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.EvaluationDTO;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.Evaluation;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.repository.EvaluationRepository;
import com.izzydrive.backend.service.EvaluationService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;

    private final DrivingRepository drivingRepository;

    @Override
    public List<Evaluation> findAll() {
        return evaluationRepository.findAll();
    }

    @Override
    public void addEvaluation(EvaluationDTO evalutionDTO) {
        Driving driving = drivingRepository.findById(evalutionDTO.getDrivingId())
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST));
        evaluationRepository.save(new Evaluation(evalutionDTO.getText(), LocalDateTime.now(), evalutionDTO.getDriverRate(), evalutionDTO.getVehicleGrade(), driving));
    }

    @Override
    public List<EvaluationDTO> findAllByDrivingId(Long drivingId) {
        return evaluationRepository.findAllByDrivingId(drivingId).stream()
                .map(e -> new EvaluationDTO(e.getText(), e.getDriving().getId(), e.getDriverRate(), e.getVehicleGrade(), e.getTimestamp()))
                .collect(Collectors.toList());
    }
}
