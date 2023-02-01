package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.service.driving.routes.DrivingRoutesServiceImpl;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.service.users.driver.location.DriverLocationService;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesService;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static com.izzydrive.backend.utils.HelperMapper.mockLocationsDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class DriverRoutesServiceTest {

    @MockBean
    private DriverLocationService driverLocationService;

    @MockBean
    private MapService mapService;

    @Autowired
    @InjectMocks
    private DriverRoutesServiceImpl driverRoutesService;

    //getCurrentRouteFromDriverLocationToStart
    @Test
    public void should_return_route_from_driver_location_to_driving_start_location(){
            LocationDTO locationDTO = new LocationDTO(19.812617, 45.231324);
            Mockito.when(driverLocationService.getDriverLocation(DriverConst.D_MIKA_EMAIL)).thenReturn(locationDTO);
            AddressOnMapDTO firstPoint = new AddressOnMapDTO(locationDTO.getLon(), locationDTO.getLat());

            CalculatedRouteDTO calculatedRouteDTO = new CalculatedRouteDTO();
            calculatedRouteDTO.setCoordinates(mockLocationsDTO());
            calculatedRouteDTO.setDistance(1000);
            calculatedRouteDTO.setDuration(5);
            AddressOnMapDTO startLocation = new AddressOnMapDTO();
            startLocation.setLatitude(45.231224);
            startLocation.setLongitude(19.812517);
            Mockito.when(mapService.getCalculatedRoutesFromPoints(List.of(firstPoint, startLocation))).thenReturn(List.of(calculatedRouteDTO));

            Driver driver = new Driver();
            driver.setEmail(DriverConst.D_MIKA_EMAIL);

            CalculatedRouteDTO result = this.driverRoutesService.getCurrentRouteFromDriverLocationToStart(driver, startLocation);

            assertEquals(calculatedRouteDTO.getDuration(), result.getDuration());
            assertEquals(calculatedRouteDTO.getDistance(), result.getDistance());
            verify(driverLocationService, times(1)).getDriverLocation(DriverConst.D_MIKA_EMAIL);
            verify(mapService, times(1)).getCalculatedRoutesFromPoints(List.of(firstPoint, startLocation));

    }
}
