package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.service.users.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/maps")
@AllArgsConstructor
public class MapController {

    private final MapService mapService;

    private final DriverService driverService;

    @GetMapping("/address-from-text/{text}")
    public ResponseEntity<List<AddressOnMapDTO>> findAddressesOnMapFromText(@PathVariable String text) {
        List<AddressOnMapDTO> addresses = mapService.getAddressesOnMapFromText(text);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/address-from-coords")
    public ResponseEntity<AddressOnMapDTO> findAddressByCoords(@RequestParam Double lat, @RequestParam Double lon) {
        AddressOnMapDTO address = mapService.getAddressOnMapFromCoords(lat, lon);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @GetMapping("/routes-from-two-points")
    public ResponseEntity<List<CalculatedRouteDTO>> findRoutesFromTwoCoords(@RequestParam Double latP1,
                                                                            @RequestParam Double lonP1,
                                                                            @RequestParam Double latP2,
                                                                            @RequestParam Double lonP2) {
        List<CalculatedRouteDTO> calculatedRoutes =
                mapService.getCalculatedRoutesFromTwoCoords(
                        new AddressOnMapDTO(lonP1, latP1),
                        new AddressOnMapDTO(lonP2, latP2)
                );
        return new ResponseEntity<>(calculatedRoutes, HttpStatus.OK);
    }
}
