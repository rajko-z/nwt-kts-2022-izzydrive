package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.RouteDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.exception.BadRequestException;
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
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.izzydrive.backend.utils.HelperMapper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DrivingExecutionServiceTest {

    @InjectMocks
    private DrivingExecutionServiceImpl drivingExecutionService;

    @Mock
    private NavigationService navigationService;

    @Mock
    private DriverNotificationService driverNotificationService;

    @Mock
    private DrivingService drivingService;

    @Mock
    private DriverRoutesService driverRoutesService;

    @Mock
    private PassengerNotificationServiceImpl passengerNotificationService;

    @Mock
    private DriverService driverService;
    private static final Long CURRENT_DRIVING_ID = 1L;
    private static final Long NEXT_DRIVING_ID = 2L;

    @Test
    public void should_be_driver_status_free_when_next_driving_is_null() {
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
    public void should_be_driver_status_taken_when_next_driving_is_not_null() {
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

    @Test
    void should_throw_bad_exp_when_driving_is_null() {
        Driver driver = mockDriver(DriverConst.D_MILAN_EMAIL, null, true, null);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);
        Mockito.when(driverService.getCurrentDriving()).thenReturn(null);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.drivingExecutionService.startDriving());
        assertEquals("You don't have current waiting driving to start", exception.getMessage());
    }

    @Test
    void should_throw_bad_exp_when_driving_has_not_state_waiting() {
        Driver driver = mockDriver(DriverConst.D_MILAN_EMAIL, null, true, null);
        DriverDTO driverDTO = mockDriverWithLocation(DriverConst.D_MILAN_EMAIL);
        DrivingDTOWithLocations driving = mockDrivingWithNoLocations(1L, DrivingState.PAYMENT, driverDTO);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);
        Mockito.when(driverService.getCurrentDriving()).thenReturn(driving);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.drivingExecutionService.startDriving());
        assertEquals("You don't have current waiting driving to start", exception.getMessage());
    }

    @Test
    void should_throw_bad_exp_when_driver_is_not_on_start() {
        Driver driver = mockDriver(DriverConst.D_MILAN_EMAIL, null, true, null);
        DriverDTO driverDTO = mockDriverWithLocation(DriverConst.D_MILAN_EMAIL);
        DrivingDTOWithLocations driving = mockDrivingWithLocations(1L, DrivingState.WAITING, driverDTO);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);
        Mockito.when(driverService.getCurrentDriving()).thenReturn(driving);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.drivingExecutionService.startDriving());
        assertEquals("You can't start driving because you didn't arrived at start location", exception.getMessage());
    }

    @Test
    void should_throw_bad_exp_when_driver_is_on_start() { //passenger John
        Driver driver = mockDriverWithCurrentDriving(DriverConst.D_MILAN_EMAIL);
        DriverDTO driverDTO = mockDriverWithLocation(DriverConst.D_MILAN_EMAIL);
        DrivingDTOWithLocations driving = mockDrivingWithNoLocations(1L, DrivingState.WAITING, driverDTO);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);
        Mockito.when(driverService.getCurrentDriving()).thenReturn(driving);
        this.drivingExecutionService.startDriving();
        assertEquals(DriverStatus.ACTIVE, driver.getDriverStatus());
        assertEquals(DrivingState.ACTIVE, driver.getCurrentDriving().getDrivingState());
        assertNotNull(driver.getCurrentDriving().getStartDate());
        verify(passengerNotificationService, times(1)).sendSignalThatRideHasStart(driving.getPassengers());
        verify(navigationService, times(1)).startNavigationForDriver(driving, false);
    }

    private Driving mockDriving(Long id, DrivingState drivingState) {
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

    private Driver mockDriver(String email, Driving driving, boolean isActive, Driving nextDriving) {
        Driver driver = new Driver();
        driver.setEmail(email);
        driver.setCurrentDriving(driving);
        driver.setActive(isActive);
        driver.setNextDriving(nextDriving);
        return driver;
    }

    private Passenger mockPassenger(String email) {
        Passenger p = new Passenger();
        p.setEmail(email);
        return p;
    }


}
