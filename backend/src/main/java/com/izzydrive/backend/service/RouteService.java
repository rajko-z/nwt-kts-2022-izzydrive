package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.NewFavoriteRouteDTO;
import com.izzydrive.backend.dto.RouteDTO;
import java.util.List;

public interface RouteService {
    void addFavoriteRoute(NewFavoriteRouteDTO newFavoriteRouteDTO);

    List<RouteDTO> getPassengerFavoriteRides(Long passengerid);

    void removeFavoriteRoute(Long routeId, Long passengerId);

    void addToFavoriteRoute(Long routeId, Long passengerId);
}
