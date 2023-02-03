package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.dto.RouteDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.jobs.ReservationNotificationTask;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.driver.DriverStatus;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.driving.execution.DrivingExecutionServiceImpl;
import com.izzydrive.backend.service.navigation.NavigationService;
import com.izzydrive.backend.service.notification.driver.DriverNotificationService;
import com.izzydrive.backend.service.notification.passenger.PassengerNotificationServiceImpl;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static com.izzydrive.backend.utils.HelperMapper.mockLocationsDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class DrivingExecutionServiceTest {

    @Autowired
    @InjectMocks
    private DrivingExecutionServiceImpl drivingExecutionService;

    @MockBean
    private NavigationService navigationService;

    @MockBean
    private DriverNotificationService driverNotificationService;

    @MockBean
    private DrivingService drivingService;

    @MockBean
    private DriverRoutesService driverRoutesService;

    @MockBean
    private PassengerNotificationServiceImpl passengerNotificationService;

//    @MockBean
//    private Logger LOG = LoggerFactory.getLogger(ReservationNotificationTask.class);

    @MockBean
    private ReservationNotificationTask reservationNotificationTask;

    private static final String REASON = "I want to cancel driving";
    private static final Long CURRENT_DRIVING_ID = 1L;
    private static final Long NEXT_DRIVING_ID = 2L;
    private static final Long INVALID_DRIVING_ID = 3L;
    private static final Long NO_MATCHING_DRIVING_ID = 3L;


    @Test
    public void should_be_driver_status_free_when_next_driving_is_null(){
        Driving driving = this.mockDriving(CURRENT_DRIVING_ID, DrivingState.WAITING);
        Driver driver = this.mockDriver(DriverConst.D_MIKA_EMAIL, driving, true, null);  //next driving is null
        Mockito.when(drivingService.findById(CURRENT_DRIVING_ID)).thenReturn(Optional.of(driving));

        this.drivingExecutionService.stopCurrentDrivingAndMoveToNextIfExist(driver);
        assertNull(driver.getCurrentDriving());
        assertEquals(DriverStatus.FREE, driver.getDriverStatus());
        verify(navigationService, times(1)).stopNavigationForDriver(driver.getEmail());
        verify(driverNotificationService, times(1)).deleteCurrentDrivingSignal(driver.getEmail());
    }

    @Test
    public void should_be_driver_status_taken_when_next_driving_is_not_null(){
        Driving currentDriving = this.mockDriving(CURRENT_DRIVING_ID, DrivingState.ACTIVE);
        Driving nextDriving = this.mockDriving(NEXT_DRIVING_ID, DrivingState.WAITING);
        Driver driver = this.mockDriver(DriverConst.D_MIKA_EMAIL, currentDriving, true, nextDriving);  //next driving is not null
        Mockito.when(drivingService.findById(CURRENT_DRIVING_ID)).thenReturn(Optional.of(currentDriving));
        Mockito.when(drivingService.findById(NEXT_DRIVING_ID)).thenReturn(Optional.of(nextDriving));
        Mockito.when(drivingService.getDrivingWithLocations(nextDriving.getId())).thenReturn(nextDriving);
        DrivingDTOWithLocations drivingDTOWithLocations = new DrivingDTOWithLocations();
        drivingDTOWithLocations.setId(nextDriving.getId());
        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setStart(new AddressOnMapDTO(19.849130, 45.250550, "Maksima Gorkog 24 Novi Sad"));
        drivingDTOWithLocations.setRoute(routeDTO);

        Mockito.when(drivingService.findDrivingWithLocationsDTOById(driver.getNextDriving().getId())).thenReturn(drivingDTOWithLocations);
        CalculatedRouteDTO calculatedRouteDTO = new CalculatedRouteDTO();
        calculatedRouteDTO.setDuration(10);
        calculatedRouteDTO.setDistance(1200);
        List<LocationDTO> coordinates = mockLocationsDTO();
        calculatedRouteDTO.setCoordinates(coordinates);
        Mockito.when(driverRoutesService.getCurrentRouteFromDriverLocationToStart(driver, drivingDTOWithLocations.getRoute().getStart())).thenReturn(calculatedRouteDTO);

        this.drivingExecutionService.stopCurrentDrivingAndMoveToNextIfExist(driver);

        assertEquals(nextDriving.getId(), driver.getCurrentDriving().getId());
        assertEquals(DriverStatus.TAKEN, driver.getDriverStatus());
        verify(navigationService, times(1)).stopNavigationForDriver(driver.getEmail());
        verify(driverNotificationService, times(1)).sendCurrentDrivingToDriver(drivingDTOWithLocations);
        verify(driverNotificationService, times(1)).deleteNextDrivingSignal(driver.getEmail());
        verify(passengerNotificationService, times(1)).sendRefreshedDriving(drivingDTOWithLocations);
        verify(navigationService, times(1)).startNavigationForDriver(drivingDTOWithLocations, true);
    }

    private Driving mockDriving(Long id, DrivingState drivingState){
        Driving driving = new Driving();
        driving.setId(id);
        driving.setDrivingState(drivingState);
        driving.setReservation(false);
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(this.mockPassenger(PassengerConst.P_BOB_EMAIL));
        passengers.add(this.mockPassenger(PassengerConst.P_JOHN_EMAIL));
        driving.setPassengers(passengers);
        return driving;
    }

    private Driver mockDriver(String email, Driving driving, boolean isActive, Driving nextDriving){
        Driver driver = new Driver();
        driver.setEmail(email);
        driver.setCurrentDriving(driving);
        driver.setActive(isActive);
        driver.setNextDriving(nextDriving);
        return  driver;
    }

    private Passenger mockPassenger(String email){
        Passenger p = new Passenger();
        p.setEmail(email);
        return p;
    }


}
