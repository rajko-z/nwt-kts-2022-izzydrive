package com.izzydrive.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EvaluationDTO {
    private String text;
    private Long drivingId;
    private Double driverRate;
    private Double vehicleGrade;
    private LocalDateTime timeStamp;

}
