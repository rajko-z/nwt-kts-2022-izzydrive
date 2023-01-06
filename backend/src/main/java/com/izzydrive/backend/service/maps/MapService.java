package com.izzydrive.backend.service.maps;

import com.izzydrive.backend.dto.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;

import java.util.List;

public interface MapService {
    List<AddressOnMapDTO> getAddressesOnMapFromText(String text);
    AddressOnMapDTO getAddressOnMapFromCoords(double lat, double lon);
    List<CalculatedRouteDTO> getCalculatedRoutesFromTwoCoords(AddressOnMapDTO point1, AddressOnMapDTO point2);
}
