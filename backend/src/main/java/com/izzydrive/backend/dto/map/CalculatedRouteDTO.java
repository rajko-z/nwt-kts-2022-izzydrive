package com.izzydrive.backend.dto.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalculatedRouteDTO {
    private List<LocationDTO> coordinates;
    private double distance;
    private double duration;

    // this is used only when user request multiple stations and tell system calculate on its own
    private List<AddressOnMapDTO> reorderedIntermediate;

    public CalculatedRouteDTO(List<LocationDTO> coordinates, double distance, double duration) {
        this.coordinates = coordinates;
        this.distance = distance;
        this.duration = duration;
    }
}
