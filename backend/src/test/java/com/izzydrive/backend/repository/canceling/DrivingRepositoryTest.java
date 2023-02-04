package com.izzydrive.backend.repository.canceling;

import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.Route;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.repository.users.driver.DriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-cancel-driving.properties"})

public class DrivingRepositoryTest {

    @Autowired
    private DrivingRepository drivingRepository;

    private static final long PREDRAG_CURRENT_DRIVING_ID = 3l;
    private static final long PREDRAG_CURRENT_DRIVING_ROUTE_ID = 2l;
    private static final long PREDRAG_RESERVATION_DRIVING_ID = 7l;
    private static final long PREDRAG_RESERVATION_DRIVING_ROUTE_ID = 7l;

    //getDrivingByIdWithDriverRouteAndPassengers
    @Test
    public void should_return_driving_with_route_and_passengers(){
        Driving driving = this.drivingRepository.getDrivingByIdWithDriverRouteAndPassengers(PREDRAG_CURRENT_DRIVING_ID);
        Driving drivingForComparing = createCurrentDrivingPredrag();
        assertEquals(drivingForComparing.getId(), driving.getId());
        assertEquals(drivingForComparing.getDrivingState(), driving.getDrivingState());
        assertEquals(drivingForComparing.getDuration(),driving.getDuration());
        assertEquals(drivingForComparing.getRoute().getId(), driving.getRoute().getId());
        assertEquals(drivingForComparing.getPrice(),driving.getPrice());
        assertIterableEquals(drivingForComparing.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()),
                driving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
    }

    //getReservationDrivingByIdWithDriverRouteAndPassengers
    @Test
    public void should_return_reservation_driving_with_route_and_passengers(){
        Driving driving = this.drivingRepository.getReservationDrivingByIdWithDriverRouteAndPassengers(PREDRAG_RESERVATION_DRIVING_ID);
        Driving drivingForComparing = createReservedDrivingPredrag();
        assertEquals( drivingForComparing.getId(),driving.getId());
        assertEquals( drivingForComparing.getDrivingState(),driving.getDrivingState());
        assertEquals(drivingForComparing.getDuration(),driving.getDuration());
        assertEquals(drivingForComparing.getRoute().getId(), driving.getRoute().getId());
        assertEquals( drivingForComparing.getPrice(), driving.getPrice());
        assertIterableEquals( drivingForComparing.getAllPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()),
                driving.getAllPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
        assertTrue(driving.isReservation());
    }

    public static Driving createCurrentDrivingPredrag(){
        Driving driving = new Driving();
        driving.setId(PREDRAG_CURRENT_DRIVING_ID);
        driving.setRoute(createRoute(PREDRAG_CURRENT_DRIVING_ROUTE_ID));
        driving.setPassengers(Set.of(createPassenger(PassengerConst.P_SARA_EMAIL)));
        driving.setDrivingState(DrivingState.PAYMENT);
        driving.setDuration(317.5);
        driving.setPrice(550);
        driving.setReservation(false);
        driving.setVersion(1);
        return driving;
    }

    public static Driving createReservedDrivingPredrag(){
        Driving driving = new Driving();
        driving.setId(PREDRAG_RESERVATION_DRIVING_ID);
        driving.setRoute(createRoute(PREDRAG_RESERVATION_DRIVING_ROUTE_ID));
        driving.setAllPassengers(List.of(createPassenger(PassengerConst.P_SARA_EMAIL)));
        driving.setDrivingState(DrivingState.WAITING);
        driving.setDuration(313.7);
        driving.setPrice(500);
        driving.setReservation(true);
        driving.setVersion(1);
        return driving;
    }

    public static Route createRoute(Long routeId){
        Route route = new Route();
        route.setId(routeId);
        Address start = new Address( 19.8255683,45.2405031, "73, Цара Душана, Адамовићево насеље, МЗ 7. Јули, Нови Сад");
        Address end = new Address(19.8411433,45.2542534, "6, Шафарикова, Роткварија, МЗ Житни трг, Нови Сад");
        Address middleStation1 = new Address(19.824673, 45.2501044,"8, Хајдук Вељкова, Сајмиште, МЗ Народни хероји, Нови Сад");
        Address middleStation2 = new Address(19.8276181,45.2542956, "33, Новосадског Сајма, Сајмиште, МЗ Народни хероји, Нови Сад");
        route.setStart(start);
        route.setEnd(end);
        route.setIntermediateStations(List.of(middleStation1,middleStation2));
        return route;
    }

    public static Passenger createPassenger(String email){
        Passenger passenger  = new Passenger();
        passenger.setEmail(email);
        return passenger;
    }
}
