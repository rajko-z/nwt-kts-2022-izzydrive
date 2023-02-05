package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.users.driver.DriverLocation;
import com.izzydrive.backend.repository.DriverLocationRepository;
import com.izzydrive.backend.service.users.driver.location.DriverLocationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DriverLocationServiceTest {

    @Mock
    private DriverLocationRepository driverLocationRepository;

    @InjectMocks
    private DriverLocationServiceImpl driverLocationService;

    //getDriverLocation(String driverEmail)
    @Test
    public void should_return_driver_location(){
        double lat = 45.231324;
        double lon = 19.812617;
        DriverLocation location = new DriverLocation();
        location.setId(1L);
        location.setLat(lat);
        location.setLon(lon);
        LocationDTO locationDTO = new LocationDTO(lon, lat);
        Mockito.when(driverLocationRepository.findByEmail(DriverConst.D_MIKA_EMAIL)).thenReturn(location);

        LocationDTO result = this.driverLocationService.getDriverLocation(DriverConst.D_MIKA_EMAIL);
        assertEquals(locationDTO.getLon(), result.getLon());
        assertEquals(locationDTO.getLat(), result.getLat());
        verify(driverLocationRepository, times(1)).findByEmail(DriverConst.D_MIKA_EMAIL);
    }
}
