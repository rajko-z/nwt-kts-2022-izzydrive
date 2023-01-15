package com.izzydrive.backend.dto;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteDTO {
    private AddressOnMapDTO start;
    private AddressOnMapDTO end;
    private List<AddressOnMapDTO> intermediateStations;
}
