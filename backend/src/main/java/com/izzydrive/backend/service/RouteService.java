package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.RouteDTO;

import java.util.List;

public interface RouteService {

    List<RouteDTO> getPassengerFavoriteRides(Long passengerid);

    void removeFavoriteRoute(Long routeId, Long passengerId);

    void addFavoriteRoute(Long routeId);
}
