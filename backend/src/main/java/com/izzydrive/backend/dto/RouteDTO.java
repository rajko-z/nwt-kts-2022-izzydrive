package com.izzydrive.backend.dto;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteDTO {
    private Long id;
    private AddressOnMapDTO start;
    private AddressOnMapDTO end;
    private List<AddressOnMapDTO> intermediateStations;
}
