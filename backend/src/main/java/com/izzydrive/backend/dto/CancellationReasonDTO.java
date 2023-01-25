package com.izzydrive.backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CancellationReasonDTO {
    private String text;
    private Long drivingId;
}
