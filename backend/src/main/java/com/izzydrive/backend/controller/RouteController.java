package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.NewFavoriteRouteDTO;
import com.izzydrive.backend.dto.RouteDTO;
import com.izzydrive.backend.service.RouteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/routes")
@AllArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PostMapping("addFavorite")
    public ResponseEntity<String> addFavoriteRoute(@RequestBody @Valid NewFavoriteRouteDTO favoriteRouteDTO){
        routeService.addFavoriteRoute(favoriteRouteDTO);
        return new ResponseEntity<>("You have successfully saved the route as a favorite", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("favorite-routes/{passengerId}")
    public ResponseEntity<List<RouteDTO>> getPassengerFavoriteRides(@PathVariable Long  passengerId){
        List<RouteDTO> favoriteRouts = this.routeService.getPassengerFavoriteRides(passengerId);
        return new ResponseEntity<>(favoriteRouts, HttpStatus.OK);
    }

}
