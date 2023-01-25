package com.izzydrive.backend.dto.reports;

import lombok.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DrivingReportDTO {

    ArrayList<DrivingNumberReportItem> drivingsNumber;
    Double averageDrivingsNumber;
    Integer sumDrivingsNumber;
    ArrayList<DrivingPriceReportItem> drivingPrices;
    Double averageDrivingPrice;
    Double sumDrivingPrice;
    ArrayList<DrivingDistanceReportItem> drivingsDistances;
    Double averageDrivingDistance;
    Double sumDrivingDistance;

    public void setAverageDrivingDistance(Double averageDrivingDistance) {
        DecimalFormat df = new DecimalFormat("#.##");
        this.averageDrivingDistance = Double.valueOf(df.format(averageDrivingDistance));
    }

    public void setAverageDrivingPrice(Double averageDrivingPrice) {
        DecimalFormat df = new DecimalFormat("#.##");
        this.averageDrivingPrice = Double.valueOf(df.format(averageDrivingPrice));
    }

    public void setAverageDrivingsNumber(Double averageDrivingsNumber) {
        DecimalFormat df = new DecimalFormat("#.##");
        this.averageDrivingsNumber = Double.valueOf(df.format(averageDrivingsNumber));
    }

    public void setSumDrivingDistance(Double sumDrivingDistance) {
        DecimalFormat df = new DecimalFormat("#.##");
        this.sumDrivingDistance = Double.valueOf(df.format(sumDrivingDistance));
    }
    public void setSumDrivingPrice(Double sumDrivingPrice) {
        DecimalFormat df = new DecimalFormat("#.##");
        this.sumDrivingPrice = Double.valueOf(df.format(sumDrivingPrice));
    }
}
