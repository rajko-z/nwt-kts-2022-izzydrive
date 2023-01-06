package com.izzydrive.backend.service.maps;

import com.izzydrive.backend.dto.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.dto.osm.OSRMRoutesPathDTO;
import com.izzydrive.backend.dto.osm.PlaceDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class OSMScraper {

    @Value("${osm-nominatim-address-from-text-url}")
    private String addressFromTextUrl;

    @Value("${osm-nominatim-address-from-coords-url}")
    private String addressFromCoordsUrl;

    @Value("${osrm-calculate-route-from-coords-base-url}")
    private String calculateRouteOSRMBaseUrl;

    @Value("${osrm-calculate-route-from-coords-suffix-url}")
    private String calculateRouteOSRMSuffixUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<AddressOnMapDTO> getAddressesOnMapFromText(String text) {
        List<PlaceDTO> places = Arrays.asList(Objects.requireNonNull(restTemplate
                .getForObject(addressFromTextUrl + "/" + text, PlaceDTO[].class)));

        return places.stream()
                .map(p -> new AddressOnMapDTO(p.getLon(), p.getLat(), p.getDisplay_name()))
                .collect(Collectors.toList());
    }

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

    public List<CalculatedRouteDTO> getCalculatedRoutesFromTwoPoints(AddressOnMapDTO point1, AddressOnMapDTO point2) {
        String url = String.format("%s%f,%f;%f,%f%s",
                calculateRouteOSRMBaseUrl,
                point1.getLongitude(), point1.getLatitude(), point2.getLongitude(), point2.getLatitude(),
                calculateRouteOSRMSuffixUrl);

        OSRMRoutesPathDTO osrm = restTemplate.getForObject(url, OSRMRoutesPathDTO.class);
        return Objects.requireNonNull(osrm).getRoutes().stream()
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


}
