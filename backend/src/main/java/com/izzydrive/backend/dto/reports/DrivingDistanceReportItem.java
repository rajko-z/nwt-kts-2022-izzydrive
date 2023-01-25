package com.izzydrive.backend.dto.reports;

import lombok.*;

import java.text.DecimalFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DrivingDistanceReportItem {
    Double distance;
    String date;

    public void setDistance(Double distance) {
        DecimalFormat df = new DecimalFormat("#.##");
        this.distance = Double.valueOf(df.format(distance));
    }
}
