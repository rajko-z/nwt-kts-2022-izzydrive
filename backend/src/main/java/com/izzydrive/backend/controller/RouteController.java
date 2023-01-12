package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.NewFavoriteRouteDTO;
import com.izzydrive.backend.service.RouteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
}
