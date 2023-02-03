package com.izzydrive.backend.utils;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.*;
import com.izzydrive.backend.model.car.Car;
import com.izzydrive.backend.model.car.CarType;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.driver.DriverStatus;

import java.util.*;

public class HelperMapper {

    public static Driving mockDrivingWithRoute(Long id, DrivingState drivingState){
        Driving driving = new Driving();
        driving.setId(id);
        driving.setDrivingState(drivingState);
        driving.setReservation(false);
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(mockPassenger(PassengerConst.P_BOB_EMAIL));
        passengers.add(mockPassenger(PassengerConst.P_JOHN_EMAIL));
        driving.setPassengers(passengers);
        driving.setAllPassengers(new ArrayList<>(passengers));
        return driving;
    }

    public static Driver mockDriver(String email, Driving driving, boolean isActive, Driving nextDriving){
        Driver driver = new Driver();
        driver.setEmail(email);
        driver.setCurrentDriving(driving);
        driver.setActive(isActive);
        driver.setNextDriving(nextDriving);
        return  driver;
    }

    public static Passenger mockPassenger(String email){
        Passenger p = new Passenger();
        p.setEmail(email);
        return p;
    }

    public static Passenger mockPassengerWithCurrentDriving(String email, DrivingState state){
        Passenger p = new Passenger();
        p.setEmail(email);
        p.setCurrentDriving(mockDriving(1L, state));
        return p;
    }

    public static Driving mockDriving(Long id, DrivingState drivingState){
        Driver driver = mockDriverForDriving(DriverConst.D_MILAN_EMAIL);
        Driving driving = new Driving();
        driving.setId(id);
        driving.setDrivingState(drivingState);
        driving.setDuration(441.2);
        driving.setPrice(250);
        List<Location> coordinates = mockLocations();
        driving.setLocations(coordinates);
        Address start = new Address(19.812617, 45.231324, "Ulica Petra Petrovica Novi Sad");
        Address end = new Address(19.809645, 45.23218, "Ulica Marka Markovica Novi Sad");
        Route route = new Route(start, end);
        route.setIntermediateStations(new ArrayList<>());
        driving.setRoute(route);
        driving.setDriver(driver);
        driver.setCurrentDriving(driving);
        return driving;
    }

    public static DrivingDTOWithLocations mockDrivingWithNoLocations(Long id, DrivingState drivingState, DriverDTO driver){
        DrivingDTOWithLocations driving = new DrivingDTOWithLocations();
        driving.setId(id);
        driving.setDrivingState(drivingState);
        driving.setPrice(250);
        Address start = new Address(19.812617, 45.231324, "Ulica Petra Petrovica Novi Sad");
        Address end = new Address(19.809645, 45.23218, "Ulica Marka Markovica Novi Sad");
        Route route = new Route(start, end);
        route.setIntermediateStations(new ArrayList<>());
        CalculatedRouteDTO calculatedRouteDTO = new CalculatedRouteDTO();
        List<LocationDTO> locationDTOS = mockLocationsDTOStart();
        calculatedRouteDTO.setCoordinates(locationDTOS);
        driving.setFromDriverToStart(calculatedRouteDTO);
        driving.setDriver(driver);
        driving.setPassengers(new ArrayList<>(Collections.singleton(PassengerConst.P_JOHN_EMAIL)));
        return driving;
    }

    public static DrivingDTOWithLocations mockDrivingWithLocations(Long id, DrivingState drivingState, DriverDTO driver){
        DrivingDTOWithLocations driving = new DrivingDTOWithLocations();
        driving.setId(id);
        driving.setDrivingState(drivingState);
        driving.setPrice(250);
        Address start = new Address(19.812617, 45.231324, "Ulica Petra Petrovica Novi Sad");
        Address end = new Address(19.809645, 45.23218, "Ulica Marka Markovica Novi Sad");
        Route route = new Route(start, end);
        route.setIntermediateStations(new ArrayList<>());
        List<LocationDTO> locationDTOS = mockLocationsDTO();
        CalculatedRouteDTO calculatedRouteDTO = new CalculatedRouteDTO();
        calculatedRouteDTO.setCoordinates(locationDTOS);
        driving.setFromDriverToStart(calculatedRouteDTO);
        driving.setDriver(driver);
        return driving;
    }

    public static Driver mockDriverWithCurrentDriving(String email){
        Driver driver = new Driver();
        driver.setEmail(email);
        driver.setDriverStatus(DriverStatus.TAKEN);
        Driving driving = new Driving();
        driving.setDrivingState(DrivingState.WAITING);
        driver.setCurrentDriving(driving);
        return driver;
    }

    public static DriverDTO mockDriverWithLocation(String email){
        DriverDTO driver = new DriverDTO();
        driver.setEmail(email);
        driver.setLocation(new LocationDTO(45.23218,19.809645));
        return driver;
    }

    public static Driver mockDriverForDriving(String email){
        Driver driver = new Driver();
        driver.setEmail(email);
        driver.setActive(true);
        return  driver;
    }

    public static Driving mockDrivingWithRoute(Long id, Driver driver){
        Driving driving = new Driving();
        driving.setId(id);
        List<Location> coordinates = mockLocations();
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(mockPassenger(PassengerConst.P_BOB_EMAIL));
        passengers.add(mockPassenger(PassengerConst.P_JOHN_EMAIL));
        driving.setLocations(coordinates);
        Address start = new Address(19.812617, 45.231324, "Ulica Petra Petrovica Novi Sad");
        Address end = new Address(19.809645, 45.23218, "Ulica Marka Markovica Novi Sad");
        Route route = new Route(start, end);
        route.setIntermediateStations(new ArrayList<>());
        driving.setRoute(route);
        driving.setDriver(driver);
        driving.setDrivingState(DrivingState.WAITING);

        return driving;
    }

    public static List<Location> mockLocations(){
        List<Location> coordinates = new ArrayList<>();
        coordinates.add(new Location(45.231324,19.812617,  true));
        coordinates.add(new Location(45.23104,19.812177, true));
        coordinates.add(new Location(45.231311, 19.809474, true));
        coordinates.add(new Location(45.23218,19.809645,  true));
        return  coordinates;
    }

    public static List<LocationDTO> mockLocationsDTO(){
        List<LocationDTO> coordinates = new ArrayList<>();
        coordinates.add(new LocationDTO(19.812617, 45.231324));
        coordinates.add(new LocationDTO(19.812177, 45.23104));
        coordinates.add(new LocationDTO(19.809474, 45.231311));
        coordinates.add(new LocationDTO(19.809645, 45.23218));
        return  coordinates;
    }

    public static List<LocationDTO> mockLocationsDTOStart(){
        List<LocationDTO> coordinates = new ArrayList<>();
        coordinates.add(new LocationDTO(19.812617, 45.231324));
        coordinates.add(new LocationDTO(19.812177, 45.23104));
        return  coordinates;
    }

    public static Driver mockDriverWithCar(Long driverId){
        Driver driver = new Driver();
        driver.setEmail(DriverConst.D_MIKA_EMAIL);
        driver.setFirstName("Mika");
        driver.setLastName("Mikic");
        Car car = new Car();
        car.setCarType(CarType.REGULAR);
        car.setCarAccommodations("BABY");
        car.setRegistration("AA-121-CC");
        car.setModel("Audi A3");
        car.setMaxNumOfPassengers(2);
        driver.setCar(car);
        return driver;
    }
}
