package com.izzydrive.backend.service.maps;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.enumerations.OptimalDrivingType;

import java.util.List;

public interface MapService {
    List<AddressOnMapDTO> getAddressesOnMapFromText(String text);
    AddressOnMapDTO getAddressOnMapFromCoords(double lat, double lon);

    List<CalculatedRouteDTO> getCalculatedRoutesFromPoints(List<AddressOnMapDTO> points);

    List<CalculatedRouteDTO> getOptimalCalculatedRoutesFromPoints(List<AddressOnMapDTO> points, OptimalDrivingType optimalDrivingType);

    CalculatedRouteDTO concatRoutesIntoOne(List<CalculatedRouteDTO> routes);

    boolean addressBelongsToBoundingBoxOfNS(AddressOnMapDTO addressOnMapDTO);
}
