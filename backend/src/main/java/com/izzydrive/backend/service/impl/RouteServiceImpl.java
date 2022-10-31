package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.repository.RouteRepository;
import com.izzydrive.backend.service.RouteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
}
