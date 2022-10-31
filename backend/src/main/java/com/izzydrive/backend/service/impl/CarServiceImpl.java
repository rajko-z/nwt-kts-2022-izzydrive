package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.repository.CarRepository;
import com.izzydrive.backend.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
}
