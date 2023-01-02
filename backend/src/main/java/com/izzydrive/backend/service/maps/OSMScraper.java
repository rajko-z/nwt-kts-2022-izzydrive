package com.izzydrive.backend.service.maps;

import com.izzydrive.backend.dto.AddressOnMapDTO;
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


}
