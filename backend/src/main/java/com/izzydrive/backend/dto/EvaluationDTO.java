package com.izzydrive.backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EvaluationDTO {
    private String text;
    private Long drivingId;
    private Double rate;

}
