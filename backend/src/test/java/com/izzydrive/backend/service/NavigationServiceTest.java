package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.service.navigation.NavigationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static com.izzydrive.backend.utils.HelperMapper.mockDriverWithLocation;
import static com.izzydrive.backend.utils.HelperMapper.mockDrivingWithNoLocations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NavigationServiceTest {

    @InjectMocks
    private NavigationServiceImpl navigationService;

    @Mock
    private  ThreadPoolTaskExecutor threadPoolExecutor;

    //stopNavigationForDriver
    @Test
    public void should_stop_navigation_for_driver(){

    }

    //startNavigationForDriver
    @Test
    public void should_start_navigation_for_driver(){
        DriverDTO driver = new DriverDTO();
        driver.setEmail(DriverConst.D_MIKA_EMAIL);
        DrivingDTOWithLocations drivingDTOWithLocations = new DrivingDTOWithLocations();
        drivingDTOWithLocations.setDriver(driver);
        navigationService.startNavigationForDriver(drivingDTOWithLocations, true);
        verify(threadPoolExecutor, times(1)).execute(any());
    }

    @Test
    void should_start_navigation_for_driver_start(){
        DriverDTO driverDTO = mockDriverWithLocation(DriverConst.D_MILAN_EMAIL);
        DrivingDTOWithLocations driving = mockDrivingWithNoLocations(1L, DrivingState.WAITING, driverDTO);
        navigationService.startNavigationForDriver(driving, false);
        verify(threadPoolExecutor, times(1)).execute(any());
    }

}
