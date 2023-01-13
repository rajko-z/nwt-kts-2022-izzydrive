package com.izzydrive.backend.service.maps.impl;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.dto.osm.OSRMRoutesPathDTO;
import com.izzydrive.backend.dto.osm.PlaceDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.service.maps.OSMScraper;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class OSMScraperImpl implements OSMScraper {

    @Value("${osm-nominatim-address-from-text-url}")
    private String addressFromTextUrl;

    @Value("${osm-nominatim-address-from-coords-url}")
    private String addressFromCoordsUrl;

    @Value("${osrm-calculate-route-from-coords-base-url}")
    private String calculateRouteOSRMBaseUrl;

    @Value("${osrm-calculate-route-from-coords-suffix-url}")
    private String calculateRouteOSRMSuffixUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<AddressOnMapDTO> getAddressesOnMapFromText(String text) {
        PlaceDTO[] placesArray = restTemplate.getForObject(addressFromTextUrl + "/" + text, PlaceDTO[].class);

        if (placesArray == null) {
            throw new BadRequestException(ExceptionMessageConstants.cantLocatePlaceForText(text));
        }

        return Arrays.stream(placesArray)
                .map(p -> new AddressOnMapDTO(p.getLon(), p.getLat(), p.getDisplay_name()))
                .collect(Collectors.toList());
    }

    @Override
    public AddressOnMapDTO getAddressOnMapFromCoords(double lon, double lat) {
        PlaceDTO place = restTemplate.getForObject(addressFromCoordsUrl +
                String.format("lat=%s&lon=%s",lat, lon), PlaceDTO.class);

        if (place == null) {
            throw new BadRequestException(ExceptionMessageConstants.placeForGeoCodeDoesNotExist(lon,lat));
        }
        if (place.getError() != null && !place.getError().isBlank()) {
            throw  new BadRequestException(ExceptionMessageConstants.placeForGeoCodeDoesNotExist(lon,lat));
        }

        return new AddressOnMapDTO(place.getLon(), place.getLat(), place.getDisplay_name());
    }

    @Override
    public List<CalculatedRouteDTO> getCalculatedRoutesFromPoints(List<AddressOnMapDTO> points) {
        String url = String.format("%s%s%s",
                                    calculateRouteOSRMBaseUrl,
                                    formatPointsForOSRMRequest(points),
                                    calculateRouteOSRMSuffixUrl);

        OSRMRoutesPathDTO osrm = restTemplate.getForObject(url, OSRMRoutesPathDTO.class);
        if (osrm == null) {
            throw new BadRequestException(ExceptionMessageConstants.ERROR_HAPPENED_WHILE_CALCULATING_ROUTES);
        }

        return osrm.getRoutes().stream()
                .map(route ->
                        new CalculatedRouteDTO(
                                route.getGeometry()
                                        .getCoordinates()
                                        .stream()
                                        .map(coords -> new LocationDTO(coords[0], coords[1]))
                                        .collect(Collectors.toList()),
                                route.getDistance(),
                                route.getDuration())
                ).collect(Collectors.toList());
    }

    private String formatPointsForOSRMRequest(List<AddressOnMapDTO> points) {
        StringBuilder sb = new StringBuilder();
        for (AddressOnMapDTO p : points) {
            sb.append(p.getLongitude());
            sb.append(",");
            sb.append(p.getLatitude());
            sb.append(";");
        }
        String retVal = sb.toString();
        return retVal.substring(0, retVal.length() - 1);
    }

}
