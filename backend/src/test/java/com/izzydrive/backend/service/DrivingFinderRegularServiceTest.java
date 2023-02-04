package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.model.car.Car;
import com.izzydrive.backend.model.car.CarType;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.driver.DriverStatus;
import com.izzydrive.backend.service.driving.validation.DrivingValidationServiceImpl;
import com.izzydrive.backend.service.drivingfinder.helper.DrivingFinderHelperImpl;
import com.izzydrive.backend.service.drivingfinder.regular.DrivingFinderRegularServiceImpl;
import com.izzydrive.backend.service.users.driver.DriverServiceImpl;
import com.izzydrive.backend.service.users.driver.car.CarServiceImpl;
import com.izzydrive.backend.service.users.driver.location.DriverLocationServiceImpl;
import com.izzydrive.backend.service.users.driver.locker.DriverLockerServiceImpl;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesServiceImpl;
import com.izzydrive.backend.service.users.driver.workingtime.validation.DriverWorkTimeValidationServiceImpl;
import com.izzydrive.backend.utils.AddressesUtil;
import com.izzydrive.backend.utils.CalculatedRouteUtil;
import com.izzydrive.backend.utils.DrivingFinderUtil;
import com.izzydrive.backend.utils.HelperMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class DrivingFinderRegularServiceTest {

    private static final String TEST_DRIVER_EMAIL_1 = "test1@gmail.com";

    @Autowired
    @InjectMocks
    private DrivingFinderRegularServiceImpl drivingFinderRegularService;

    @MockBean
    private DriverServiceImpl driverService;

    @MockBean
    private CarServiceImpl carService;

    @MockBean
    private DriverLockerServiceImpl driverLockerService;

    @MockBean
    private DrivingValidationServiceImpl drivingValidationService;

    @MockBean
    private DriverRoutesServiceImpl driverRoutesService;

    @MockBean
    private DriverWorkTimeValidationServiceImpl driverWorkTimeValidationService;

    @MockBean
    private DrivingFinderHelperImpl drivingFinderHelper;

    @MockBean
    private DriverLocationServiceImpl driverLocationService;


    @Test
    public void should_return_empty_list_because_no_driver_available() {
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();

        List<AddressOnMapDTO> pointsFromRequest = new ArrayList<>();
        pointsFromRequest.add(request.getStartLocation());
        pointsFromRequest.add(request.getEndLocation());
        when(drivingFinderHelper.getAllPointsFromDrivingFinderRequest(request)).thenReturn(pointsFromRequest);

        List<CalculatedRouteDTO> routesFromStartToEnd = new ArrayList<>();
        routesFromStartToEnd.add(CalculatedRouteUtil.getExampleOfCalculatedRoute());
        when(drivingFinderHelper.getCalculatedRoutesFromStartToEnd(pointsFromRequest, request.getOptimalDrivingType(), request.getIntermediateStationsOrderType()))
                .thenReturn(routesFromStartToEnd);

        when(driverLockerService.driverIsLocked(anyString())).thenReturn(true);
        when(driverService.findAllActiveDrivers()).thenReturn(new ArrayList<>());

        List<DrivingOptionDTO> options = drivingFinderRegularService.getAdvancedDrivingOptions(request);

        verify(drivingFinderHelper).sortOptionsByCriteria(any(), any(), any());
        assertTrue(options.isEmpty());
    }

    @Test
    public void should_return_empty_list_because_all_drivers_reserved() {
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();

        List<AddressOnMapDTO> pointsFromRequest = new ArrayList<>();
        pointsFromRequest.add(request.getStartLocation());
        pointsFromRequest.add(request.getEndLocation());
        when(drivingFinderHelper.getAllPointsFromDrivingFinderRequest(request)).thenReturn(pointsFromRequest);

        List<CalculatedRouteDTO> routesFromStartToEnd = new ArrayList<>();
        routesFromStartToEnd.add(CalculatedRouteUtil.getExampleOfCalculatedRoute());
        when(drivingFinderHelper.getCalculatedRoutesFromStartToEnd(pointsFromRequest, request.getOptimalDrivingType(), request.getIntermediateStationsOrderType()))
                .thenReturn(routesFromStartToEnd);

        List<Driver> drivers = new ArrayList<>();
        Driver d1 = new Driver();
        Driver d2 = new Driver();
        d1.setDriverStatus(DriverStatus.RESERVED);
        d2.setDriverStatus(DriverStatus.RESERVED);
        drivers.add(d1);
        drivers.add(d2);
        when(driverService.findAllActiveDrivers()).thenReturn(drivers);

        List<DrivingOptionDTO> options = drivingFinderRegularService.getAdvancedDrivingOptions(request);

        verify(drivingFinderHelper).sortOptionsByCriteria(any(), any(), any());
        assertTrue(options.isEmpty());
    }

    @Test
    public void should_return_one_element_for_one_available_driver() {
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();

        List<AddressOnMapDTO> pointsFromRequest = new ArrayList<>();
        pointsFromRequest.add(request.getStartLocation());
        pointsFromRequest.add(request.getEndLocation());
        when(drivingFinderHelper.getAllPointsFromDrivingFinderRequest(request)).thenReturn(pointsFromRequest);

        List<CalculatedRouteDTO> routesFromStartToEnd = new ArrayList<>();
        routesFromStartToEnd.add(CalculatedRouteUtil.getExampleOfCalculatedRoute());
        when(drivingFinderHelper.getCalculatedRoutesFromStartToEnd(pointsFromRequest, request.getOptimalDrivingType(), request.getIntermediateStationsOrderType()))
                .thenReturn(routesFromStartToEnd);

        when(driverLockerService.driverIsLocked(anyString())).thenReturn(false);
        when(driverLocationService.getDriverLocation(TEST_DRIVER_EMAIL_1)).thenReturn(AddressesUtil.getBanijskaLocation());

        CalculatedRouteDTO routeFromDriverToStart = CalculatedRouteUtil.getExampleOfCalculatedRoute();
        when(driverRoutesService.getCalculatedRouteFromDriverToStart(TEST_DRIVER_EMAIL_1, request.getStartLocation()))
                .thenReturn(routeFromDriverToStart);

        when(driverWorkTimeValidationService.driverNotOutwork(any(), any(), any())).thenReturn(true);
        when(driverWorkTimeValidationService.driverOnTimeForFutureDrivingRegular(any(), any(),any(), any())).thenReturn(true);
        when(carService.calculatePrice(any(), anyDouble())).thenReturn(100.0);

        List<Driver> drivers = new ArrayList<>();
        Driver d1 = new Driver();
        d1.setDriverStatus(DriverStatus.RESERVED);
        Driver d2 = new Driver();
        d2.setDriverStatus(DriverStatus.RESERVED);
        Driver d3 = HelperMapper.mockDriver(TEST_DRIVER_EMAIL_1, null, true, null);
        d3.setDriverStatus(DriverStatus.FREE);
        Car carForD3 = new Car();
        carForD3.setCarType(CarType.REGULAR);
        d3.setCar(carForD3);
        drivers.add(d1);
        drivers.add(d2);
        drivers.add(d3);

        when(driverService.findAllActiveDrivers()).thenReturn(drivers);

        List<DrivingOptionDTO> options = drivingFinderRegularService.getAdvancedDrivingOptions(request);

        verify(drivingFinderHelper).sortOptionsByCriteria(any(), any(), any());
        assertEquals(1, options.size());
        assertEquals(100, options.get(0).getPrice());
        assertEquals(TEST_DRIVER_EMAIL_1, options.get(0).getDriver().getEmail());
        assertEquals(routeFromDriverToStart, options.get(0).getDriverToStartPath());
        assertEquals(routesFromStartToEnd.get(0), options.get(0).getStartToEndPath());
    }


}
