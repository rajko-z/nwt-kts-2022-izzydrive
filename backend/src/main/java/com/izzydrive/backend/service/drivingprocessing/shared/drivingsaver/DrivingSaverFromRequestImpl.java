package com.izzydrive.backend.service.drivingprocessing.shared.drivingsaver;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.Location;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.drivingfinder.helper.DrivingFinderHelper;
import com.izzydrive.backend.service.drivingprocessing.shared.helper.ProcessingDrivingHelper;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.driver.car.CarService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
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

    private final DrivingFinderHelper drivingFinderHelper;

    private final CarService carService;

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
        CalculatedRouteDTO fromStartToEnd = getRecalculatedFromStartToEndRoute(request);
        double price = carService.calculatePrice(driver.getCar(), fromStartToEnd.getDistance());

        List<Location> fromStartToEndLocations =
                fromStartToEnd.getCoordinates()
                .stream()
                .map(l -> new Location(l.getLat(), l.getLon(), true))
                .collect(Collectors.toList());

        Driving driving = new Driving();
        driving.setCreationDate(LocalDateTime.now());
        driving.setDistance(fromStartToEnd.getDistance());
        driving.setDuration(fromStartToEnd.getDuration());
        driving.setPrice(price);
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

    private CalculatedRouteDTO getRecalculatedFromStartToEndRoute(DrivingRequestDTO request) {
        DrivingFinderRequestDTO dfRequest = request.getDrivingFinderRequest();
        DrivingOptionDTO chosenOption = request.getDrivingOption();

        List<CalculatedRouteDTO> routes = drivingFinderHelper.getCalculatedRoutesFromStartToEnd(
                drivingFinderHelper.getAllPointsFromDrivingFinderRequest(dfRequest),
                dfRequest.getOptimalDrivingType(),
                dfRequest.getIntermediateStationsOrderType()
        );

        for (CalculatedRouteDTO route : routes) {
            if (routesMatch(route, chosenOption.getStartToEndPath())) {
                return route;
            }
        }
        throw new BadRequestException(ExceptionMessageConstants.INVALID_ROUTE_PROVIDED);
    }

    private boolean routesMatch(CalculatedRouteDTO route1, CalculatedRouteDTO route2) {
        if (route1.getDuration() != route2.getDuration()) {
            return false;
        }
        if (route1.getDistance() != route2.getDistance()) {
            return false;
        }
        return route1.getCoordinates().size() == route2.getCoordinates().size();
    }
}
