package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.converters.RouteDTOConverter;
import com.izzydrive.backend.dto.RouteDTO;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Route;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.repository.AddressRepository;
import com.izzydrive.backend.repository.RouteRepository;
import com.izzydrive.backend.repository.users.PassengerRepository;
import com.izzydrive.backend.service.RouteService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final PassengerService passengerService;

    private final PassengerRepository passengerRepository;
    private final RouteRepository routeRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public void addFavoriteRoute(Long routeId) {
        Passenger passenger = passengerService.getCurrentlyLoggedPassenger();
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.routeDoesntExist()));

        List<Route> favoriteRoutes = passenger.getFavouriteRoutes();
        favoriteRoutes.add(route);
        passenger.setFavouriteRoutes(favoriteRoutes);
        passengerService.save(passenger);

    }

    @Override
    @Transactional
    public List<RouteDTO> getPassengerFavoriteRides(Long passengerId) {
        Passenger passenger = passengerRepository.findByIdWithFavoriteRoutes(passengerId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.FAVORITE_ROUTES_NOT_FOUND));

        return this.createFavoriteRouteDTO(passenger.getFavouriteRoutes());

    }

    @Override
    @Transactional
    public void removeFavoriteRoute(Long routeId, Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userDoesntExist()));
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.routeDoesntExist()));

        passenger.getFavouriteRoutes().removeIf(r -> Objects.equals(r.getId(), routeId));
        //route.getAllPassengers().removeIf(p -> Objects.equals(p.getId(), passenger.getId()));
        passengerRepository.save(passenger);
        //routeRepository.save(route);
    }

    private List<RouteDTO> createFavoriteRouteDTO(List<Route> routs) {
        List<RouteDTO> convertedRouts = new ArrayList<>();
        for (Route rout : routs) {
            convertedRouts.add(RouteDTOConverter.convert(rout));
        }
        return convertedRouts;
    }

    private List<Address> getIntermediateStations(List<String> stationNames) {
        List<Address> addresses = new ArrayList<>();
        for (String name : stationNames) {
            Address address = new Address(name);
            addressRepository.save(address);
            addresses.add(address);
        }
        return addresses;
    }
}
