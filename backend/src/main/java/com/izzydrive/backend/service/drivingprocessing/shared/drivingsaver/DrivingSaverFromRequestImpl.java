package com.izzydrive.backend.service.drivingprocessing.shared.drivingsaver;

import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.Location;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.drivingprocessing.shared.helper.ProcessingDrivingHelper;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DrivingSaverFromRequestImpl implements DrivingSaverFromRequest {

    private final ProcessingDrivingHelper processingDrivingHelper;

    private final DrivingService drivingService;

    private final PassengerService passengerService;

    private final DriverService driverService;

    @Override
    @Transactional
    public Driving makeAndSaveDrivingFromRegularRequest(DrivingRequestDTO request,
                                                        Driver driver,
                                                        Passenger passenger)
    {
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(passenger);
        passengers.addAll(processingDrivingHelper.getPassengersWithCurrentDrivingFromEmails(request.getDrivingFinderRequest().getLinkedPassengersEmails()));

        Driving driving = createBaseDriving(request, driver, passengers);
        driving.setReservation(false);
        driving.setDrivingState(DrivingState.PAYMENT);

        drivingService.save(driving);
        updatePassengersCurrentDriving(passengers, passenger, driving);
        return driving;
    }

    @Override
    @Transactional
    public Driving makeAndSaveDrivingFromReservationRequest(DrivingRequestDTO request,
                                                            Driver driver,
                                                            Passenger passenger)
    {
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(passenger);
        passengers.addAll(processingDrivingHelper.getPassengersWithDrivingsFromEmails(request.getDrivingFinderRequest().getLinkedPassengersEmails()));

        Driving driving = createBaseDriving(request, driver, passengers);
        driving.setReservation(true);
        driving.setDrivingState(DrivingState.INITIAL);
        driving.setReservationDate(request.getDrivingFinderRequest().getScheduleTime());

        drivingService.save(driving);
        passengerService.addNewDrivingToPassengersDrivings(passengers, driving);
        driver.setReservedFromClientDriving(driving);
        driverService.save(driver);
        return driving;
    }

    private Driving createBaseDriving(DrivingRequestDTO request,
                                      Driver driver,
                                      Set<Passenger> passengers)
    {
        DrivingOptionDTO option = request.getDrivingOption();
        List<Location> fromStartToEndLocations =
                option.getStartToEndPath().getCoordinates()
                .stream()
                .map(l -> new Location(l.getLat(), l.getLon(), true))
                .collect(Collectors.toList());

        Driving driving = new Driving();
        driving.setCreationDate(LocalDateTime.now());
        driving.setDistance(option.getStartToEndPath().getDistance());
        driving.setDuration(option.getStartToEndPath().getDuration());
        driving.setPrice(option.getPrice());
        driving.setRejected(false);
        driving.setRoute(processingDrivingHelper.getRouteFromRequest(request.getDrivingFinderRequest()));
        driving.setPassengers(passengers);
        driving.setLocations(fromStartToEndLocations);
        driving.setDriver(driver);
        return driving;
    }


    private void updatePassengersCurrentDriving(Set<Passenger> linkedPassengers, Passenger initiator, Driving driving) {
        initiator.setCurrentDriving(driving);
        passengerService.save(initiator);
        for (Passenger p : linkedPassengers) {
            p.setCurrentDriving(driving);
            passengerService.save(p);
        }
    }
}
