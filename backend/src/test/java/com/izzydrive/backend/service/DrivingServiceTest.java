package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.converters.DrivingConverter;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.model.*;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.service.driving.DrivingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.izzydrive.backend.utils.HelperMapper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class DrivingServiceTest {

    private static Long DRIVING_ID = 1L;

    @MockBean
    private DrivingRepository drivingRepository;

    @Autowired
    @InjectMocks
    private DrivingServiceImpl drivingService;

    //findDrivingWithLocationsDTOById
    @Test
    public void should_return_driving_with_locations_and_passengers_for_drivingID(){

        Driver driver = mockDriverWithCar(DriverConst.D_MIKA_ID);
        Driving driving = mockDrivingWithRoute(DRIVING_ID, driver);
        List<Location> locations = mockLocations();
        Mockito.when(drivingRepository.getDrivingWithLocations(DRIVING_ID)).thenReturn(driving);
        Mockito.when(drivingRepository.getDrivingByIdWithDriverRouteAndPassengers(DRIVING_ID)).thenReturn(driving);
        DrivingDTOWithLocations drivingDTOWithLocations = DrivingConverter.convertWithLocationsAndDriver(driving, locations);

        DrivingDTOWithLocations result = this.drivingService.findDrivingWithLocationsDTOById(DRIVING_ID);

        assertEquals(drivingDTOWithLocations.getId(), result.getId());
        assertIterableEquals(drivingDTOWithLocations.getPassengers(), result.getPassengers());
        assertEquals(drivingDTOWithLocations.getRoute().getStart().getLatitude(), result.getRoute().getStart().getLatitude());
        assertEquals(drivingDTOWithLocations.getRoute().getStart().getLongitude(), result.getRoute().getStart().getLongitude());
        verify(drivingRepository, times(1)).getDrivingWithLocations(DRIVING_ID);
        verify(drivingRepository, times(1)).getDrivingByIdWithDriverRouteAndPassengers(DRIVING_ID);
    }

    //getDrivingByIdWithDriverRouteAndPassengers
    @Test
    public void should_return_driving_with_routh_and_passengers_for_drivingID(){
        Driver driver = mockDriverWithCar(DriverConst.D_MIKA_ID);
        Driving driving = mockDrivingWithRoute(DRIVING_ID, driver);
        Mockito.when(drivingRepository.getDrivingByIdWithDriverRouteAndPassengers(DRIVING_ID)).thenReturn(driving);
        Driving result = this.drivingService.getDrivingByIdWithDriverRouteAndPassengers(DRIVING_ID);

        assertEquals(driving.getId(), result.getId());
        verify(drivingRepository, times(1)).getDrivingByIdWithDriverRouteAndPassengers(DRIVING_ID);
    }

    //getDrivingWithLocations
    @Test
    public void should_return_driving_with_locations_for_drivingID(){
        Driver driver = mockDriverWithCar(DriverConst.D_MIKA_ID);
        Driving driving = mockDrivingWithRoute(DRIVING_ID, driver);
        Mockito.when(drivingRepository.getDrivingWithLocations(DRIVING_ID)).thenReturn(driving);

        Driving result = this.drivingService.getDrivingWithLocations(DRIVING_ID);

        assertEquals(driving.getId(), result.getId());
        verify(drivingRepository, times(1)).getDrivingWithLocations(DRIVING_ID);
    }

    @Test
    public void should_delete_driving(){
        Driver driver = mockDriverWithCar(DriverConst.D_MIKA_ID);
        Driving driving = mockDrivingWithRoute(DRIVING_ID, driver);
        this.drivingService.delete(driving);
        verify(drivingRepository, times(1)).delete(any());

    }



}
