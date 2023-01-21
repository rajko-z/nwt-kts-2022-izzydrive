package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.converters.RouteDTOConverter;
import com.izzydrive.backend.dto.NewFavoriteRouteDTO;
import com.izzydrive.backend.dto.RouteDTO;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Route;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.AddressRepository;
import com.izzydrive.backend.repository.RouteRepository;
import com.izzydrive.backend.repository.users.PassengerRepository;
import com.izzydrive.backend.service.RouteService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final PassengerRepository passengerRepository;
    private final AddressRepository addressRepository;

    @Override
    public void addFavoriteRoute(NewFavoriteRouteDTO newFavoriteRouteDTO) {
        Address startLocation = new Address(newFavoriteRouteDTO.getStartLocation());
        Address endLocation = new Address(newFavoriteRouteDTO.getEndLocation());
        Route route = new Route(startLocation, endLocation);
        Optional<Passenger> passenger = passengerRepository.findByIdWithFavoriteRoutes(newFavoriteRouteDTO.getPassengerId());
        if(passenger.isPresent()){
            List<Route> favoriteRoutes = passenger.get().getFavouriteRoutes();
            favoriteRoutes.add(route);
            passenger.get().setFavouriteRoutes(favoriteRoutes);
            passengerRepository.save(passenger.get());
        }
        addressRepository.save(startLocation);
        addressRepository.save(endLocation);
        routeRepository.save(route);
    }

    @Override
    @Transactional
    public List<RouteDTO> getPassengerFavoriteRides(Long passengerId) {
        Passenger passenger = passengerRepository.findByIdWithFavoriteRoutes(passengerId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.FAVORITE_ROUTES_NOT_FOUND));

        return this.createFavoriteRouteDTO(passenger.getFavouriteRoutes());

    }

    private List<RouteDTO> createFavoriteRouteDTO(List<Route> routs){
        List<RouteDTO> convertedRouts = new ArrayList<>();
        for (Route rout : routs){
            convertedRouts.add(RouteDTOConverter.convert(rout));
        }
        return convertedRouts;
    }
}
