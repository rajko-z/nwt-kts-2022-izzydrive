package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.service.navigation.NavigationService;
import com.izzydrive.backend.service.navigation.NavigationServiceImpl;
import com.izzydrive.backend.service.navigation.NavigationTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(SpringExtension.class)

public class NavigationServiceTest {

    @Autowired
    @InjectMocks
    private NavigationServiceImpl navigationService;

    @MockBean
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


}
