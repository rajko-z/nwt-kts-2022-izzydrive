package com.izzydrive.backend.dto.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculatedRouteDTO {
    private List<LocationDTO> coordinates;
    private double distance;
    private double duration;
}
