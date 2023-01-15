package com.izzydrive.backend.converters;

import com.izzydrive.backend.dto.RouteDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Route;

import java.util.List;
import java.util.stream.Collectors;

public class RouteDTOConverter {
    private RouteDTOConverter () {}

    public static RouteDTO convert(Route route) {
        Address s = route.getStart();
        AddressOnMapDTO start = new AddressOnMapDTO(s.getLongitude(), s.getLatitude(), s.getName());

        Address e = route.getEnd();
        AddressOnMapDTO end = new AddressOnMapDTO(e.getLongitude(), e.getLatitude(), e.getName());

        List<AddressOnMapDTO> intermediate = route.getIntermediateStations().stream()
                .map(a -> new AddressOnMapDTO(a.getLongitude(), a.getLatitude(), a.getName())).collect(Collectors.toList());

        return RouteDTO.builder()
                .start(start)
                .end(end)
                .intermediateStations(intermediate)
                .build();
    }
}
