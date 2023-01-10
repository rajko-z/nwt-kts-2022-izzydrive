package com.izzydrive.backend.dto.map;

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
