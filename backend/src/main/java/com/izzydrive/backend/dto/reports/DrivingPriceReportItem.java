package com.izzydrive.backend.dto.reports;

import lombok.*;

import java.text.DecimalFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DrivingPriceReportItem {

    String date;
    Double price;

    public void setPrice(Double price) {
        DecimalFormat df = new DecimalFormat("#.##");
        this.price = Double.valueOf(df.format(price));
    }
}
