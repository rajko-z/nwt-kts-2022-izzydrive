package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByRegistration(String registration);
}

