package com.izzydrive.backend.dto.map;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class AddressOnMapDTO {
    private double longitude;
    private double latitude;

    @NotBlank
    private String name;

    public AddressOnMapDTO(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
