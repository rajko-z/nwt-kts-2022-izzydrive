package com.izzydrive.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DrivingNoteDTO {
    private String text;
    private String driverEmail;
    private Long drivingId;
    private LocalDateTime timestamp;
    private boolean fromPassenger;
}
