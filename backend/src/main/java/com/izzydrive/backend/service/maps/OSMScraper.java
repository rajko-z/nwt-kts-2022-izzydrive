package com.izzydrive.backend.service.maps;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;

import java.util.List;

public interface OSMScraper {

    List<CalculatedRouteDTO> getCalculatedRoutesFromPoints(List<AddressOnMapDTO> points);

    AddressOnMapDTO getAddressOnMapFromCoords(double lon, double lat);

    List<AddressOnMapDTO> getAddressesOnMapFromText(String text);
}
