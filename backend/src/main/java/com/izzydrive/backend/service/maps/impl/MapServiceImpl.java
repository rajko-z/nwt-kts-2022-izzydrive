package com.izzydrive.backend.service.maps.impl;

import com.izzydrive.backend.config.MapConfig;
import com.izzydrive.backend.dto.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.service.maps.OSMScraper;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MapServiceImpl {

    private final OSMScraper osmScraper;

    private final MapConfig mapConfig;

    public List<AddressOnMapDTO> getAddressesOnMapFromText(String text) {
        return filterAddressesFromOSM(osmScraper.getAddressesOnMapFromText(text));
    }

    public AddressOnMapDTO getAddressOnMapFromCoords(double lat, double lon) {
        AddressOnMapDTO address = osmScraper.getAddressOnMapFromCoords(lon, lat);
        List<AddressOnMapDTO> addresses = filterAddressesFromOSM(List.of(address));
        if (addresses.isEmpty()) {
            throw new BadRequestException(ExceptionMessageConstants.LOCATION_OUTSIDE_OF_NOVI_SAD);
        }
        return addresses.get(0);
    }

    private List<AddressOnMapDTO> filterAddressesFromOSM(List<AddressOnMapDTO> addresses) {
        List<AddressOnMapDTO> retVal = addresses.stream()
                .filter(this::addressBelongsToBoundingBoxOfNS).collect(Collectors.toList());

        retVal.forEach(a -> a.setName(formatAddressDetailToRemoveUnnecessaryInfo(a.getName())));
        return retVal;
    }

    private String formatAddressDetailToRemoveUnnecessaryInfo(String address) {
        String separator1 = ", Град Нови Сад,";
        String separator2 = ", Јужнобачки управни округ,";

        if (address.contains(separator1)) {
            return address.split(separator1)[0];
        }
        else if (address.contains(separator2)) {
            return address.split(separator2)[0];
        }
        return address;
    }

    private boolean addressBelongsToBoundingBoxOfNS(AddressOnMapDTO addressOnMapDTO) {
        double lat = addressOnMapDTO.getLatitude();
        double lon = addressOnMapDTO.getLongitude();

        return lat >= mapConfig.getBottomLeftLat() &&
               lat <= mapConfig.getUpperRightLat() &&
               lon >= mapConfig.getBottomLeftLon() &&
               lon <= mapConfig.getUpperRightLon();
    }

    public List<CalculatedRouteDTO> getCalculatedRoutesFromTwoCoords(AddressOnMapDTO point1, AddressOnMapDTO point2) {
        return this.osmScraper.getCalculatedRoutesFromTwoPoints(point1, point2);
    }
}
