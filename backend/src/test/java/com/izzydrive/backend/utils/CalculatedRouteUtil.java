package com.izzydrive.backend.utils;

import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;

import java.util.ArrayList;
import java.util.List;

public class CalculatedRouteUtil {

    public static CalculatedRouteDTO getExampleOfCalculatedRoute() {
        CalculatedRouteDTO c = new CalculatedRouteDTO();
        c.setDuration(10.0);
        c.setDuration(100);

        List<LocationDTO> coordinates = new ArrayList<>();
        coordinates.add(AddressesUtil.getBanijskaLocation());
        coordinates.add(AddressesUtil.getBanijskaLocation());
        coordinates.add(AddressesUtil.getBanijskaLocation());

        c.setCoordinates(coordinates);
        return c;
    }

}
