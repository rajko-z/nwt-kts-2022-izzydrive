package com.izzydrive.backend.service.maps.impl;

import com.izzydrive.backend.config.MapConfig;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.enumerations.OptimalDrivingType;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MapServiceImpl implements MapService {

    private final OSMScraperImpl osmScraper;

    private final MapConfig mapConfig;

    @Override
    public List<AddressOnMapDTO> getAddressesOnMapFromText(String text) {
        return filterAddressesFromOSM(osmScraper.getAddressesOnMapFromText(text));
    }

    @Override
    public AddressOnMapDTO getAddressOnMapFromCoords(double lat, double lon) {
        AddressOnMapDTO address = osmScraper.getAddressOnMapFromCoords(lon, lat);
        List<AddressOnMapDTO> addresses = filterAddressesFromOSM(List.of(address));
        if (addresses.isEmpty()) {
            throw new BadRequestException(ExceptionMessageConstants.LOCATION_OUTSIDE_OF_NOVI_SAD);
        }
        return addresses.get(0);
    }

    @Override
    public List<CalculatedRouteDTO> getCalculatedRoutesFromPoints(List<AddressOnMapDTO> points) {
        return this.osmScraper.getCalculatedRoutesFromPoints(points);
    }

    @Override
    public List<CalculatedRouteDTO> getOptimalCalculatedRoutesFromPoints(List<AddressOnMapDTO> points, OptimalDrivingType optimalDrivingType) {
        if (points.size() <= 3) {
            return getCalculatedRoutesFromPoints(points);
        }
        if (points.size() > 5) {
            throw new BadRequestException(ExceptionMessageConstants.ERROR_SIZE_OF_INTERMEDIATE_LOCATIONS);
        }
        return getOptimalCalculatedRouteFrom4Or5Points(points, optimalDrivingType);
    }

    @Override
    public CalculatedRouteDTO concatRoutesIntoOne(List<CalculatedRouteDTO> routes) {
        double totalDistance = 0;
        double totalDuration = 0;
        List<LocationDTO> totalCoords = new ArrayList<>();

        for (CalculatedRouteDTO route : routes) {
            totalDistance += route.getDistance();
            totalDuration += route.getDuration();
            totalCoords.addAll(route.getCoordinates());
        }
        return new CalculatedRouteDTO(totalCoords, totalDistance, totalDuration);
    }

    @Override
    public boolean addressBelongsToBoundingBoxOfNS(AddressOnMapDTO addressOnMapDTO) {
        double lat = addressOnMapDTO.getLatitude();
        double lon = addressOnMapDTO.getLongitude();

        return lat >= mapConfig.getBottomLeftLat() &&
                lat <= mapConfig.getUpperRightLat() &&
                lon >= mapConfig.getBottomLeftLon() &&
                lon <= mapConfig.getUpperRightLon();
    }

    private List<CalculatedRouteDTO> getOptimalCalculatedRouteFrom4Or5Points(List<AddressOnMapDTO> points, OptimalDrivingType optimalDrivingType) {
        AddressOnMapDTO start = points.get(0);
        AddressOnMapDTO first = points.get(1);
        AddressOnMapDTO second = points.get(2);
        AddressOnMapDTO third = points.get(3);
        AddressOnMapDTO end = points.size() == 4 ? points.get(3) : points.get(4);

        CalculatedRouteDTO startToFirst = getCalculatedRoutesFromPoints(Arrays.asList(start, first)).get(0);
        CalculatedRouteDTO startToSecond = getCalculatedRoutesFromPoints(Arrays.asList(start, second)).get(0);
        CalculatedRouteDTO firstToSecond = getCalculatedRoutesFromPoints(Arrays.asList(first, second)).get(0);
        CalculatedRouteDTO secondToFirst = getCalculatedRoutesFromPoints(Arrays.asList(second, first)).get(0);
        CalculatedRouteDTO firstToEnd = getCalculatedRoutesFromPoints(Arrays.asList(first, end)).get(0);
        CalculatedRouteDTO secondToEnd = getCalculatedRoutesFromPoints(Arrays.asList(second, end)).get(0);

        if (points.size() == 4) {
            CalculatedRouteDTO option1 = concatRoutesIntoOne(Arrays.asList(startToFirst, firstToSecond, secondToEnd));
            option1.setReorderedIntermediate(List.of(start, first, second, end));
            CalculatedRouteDTO option2 = concatRoutesIntoOne(Arrays.asList(startToSecond, secondToFirst, firstToEnd));
            option1.setReorderedIntermediate(List.of(start, second, first, end));
            return List.of(findBestMatchFromCalculatedRoutesByOptimalCriteria(Arrays.asList(option1, option2), optimalDrivingType));
        }

        CalculatedRouteDTO startToThird = getCalculatedRoutesFromPoints(Arrays.asList(start, third)).get(0);
        CalculatedRouteDTO firstToThird = getCalculatedRoutesFromPoints(Arrays.asList(first, third)).get(0);
        CalculatedRouteDTO secondToThird = getCalculatedRoutesFromPoints(Arrays.asList(second, third)).get(0);
        CalculatedRouteDTO thirdToFirst = getCalculatedRoutesFromPoints(Arrays.asList(third, first)).get(0);
        CalculatedRouteDTO thirdToSecond = getCalculatedRoutesFromPoints(Arrays.asList(third, second)).get(0);
        CalculatedRouteDTO thirdToEnd = getCalculatedRoutesFromPoints(Arrays.asList(third, end)).get(0);

        CalculatedRouteDTO option1 = concatRoutesIntoOne(Arrays.asList(startToFirst, firstToSecond, secondToThird, thirdToEnd));
        option1.setReorderedIntermediate(List.of(first, second, third));

        CalculatedRouteDTO option2 = concatRoutesIntoOne(Arrays.asList(startToFirst, firstToThird, thirdToSecond, secondToEnd));
        option2.setReorderedIntermediate(List.of(first, third, second));

        CalculatedRouteDTO option3 = concatRoutesIntoOne(Arrays.asList(startToSecond, secondToFirst, firstToThird, thirdToEnd));
        option3.setReorderedIntermediate(List.of(second, first, third));

        CalculatedRouteDTO option4 = concatRoutesIntoOne(Arrays.asList(startToSecond, secondToThird, thirdToFirst, firstToEnd));
        option4.setReorderedIntermediate(List.of(second, third, first));

        CalculatedRouteDTO option5 = concatRoutesIntoOne(Arrays.asList(startToThird, thirdToFirst, firstToSecond, secondToEnd));
        option5.setReorderedIntermediate(List.of(third, first, second));

        CalculatedRouteDTO option6 = concatRoutesIntoOne(Arrays.asList(startToThird, thirdToSecond, secondToFirst, firstToEnd));
        option6.setReorderedIntermediate(List.of(third, second, first));

        return List.of(findBestMatchFromCalculatedRoutesByOptimalCriteria(Arrays.asList(option1, option2, option3, option4, option5, option6), optimalDrivingType));
    }

    private CalculatedRouteDTO findBestMatchFromCalculatedRoutesByOptimalCriteria(List<CalculatedRouteDTO> routes, OptimalDrivingType type) {
        if (type.equals(OptimalDrivingType.CHEAPEST_RIDE)) {
            return routes.stream().min(Comparator.comparing(CalculatedRouteDTO::getDistance)).get();
        }
        return routes.stream().min(Comparator.comparing(CalculatedRouteDTO::getDuration)).get();
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
}
