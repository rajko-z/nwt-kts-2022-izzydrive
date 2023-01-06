package com.izzydrive.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressOnMapDTO {
    private double longitude;
    private double latitude;
    private String name;

    public AddressOnMapDTO(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
