package com.izzydrive.backend.dto.reports;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReportRequestDTO {

    Long userId;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
