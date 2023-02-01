package com.izzydrive.backend.utils;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.*;
import com.izzydrive.backend.model.car.Car;
import com.izzydrive.backend.model.car.CarType;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.driver.Driver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<LocationDTO> cordinates = new ArrayList<>();
        cordinates.add(new LocationDTO(19.812617, 45.231324));
        cordinates.add(new LocationDTO(19.812177, 45.23104));
        cordinates.add(new LocationDTO(19.809474, 45.231311));
        cordinates.add(new LocationDTO(19.809645, 45.23218));
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
