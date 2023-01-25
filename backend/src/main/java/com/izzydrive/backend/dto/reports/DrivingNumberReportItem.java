package com.izzydrive.backend.dto.reports;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DrivingNumberReportItem {
    Integer numberOfDrivings;
    String date;
}
