package com.izzydrive.backend.dto.map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDTO {
    private double lon;
    private double lat;
}
