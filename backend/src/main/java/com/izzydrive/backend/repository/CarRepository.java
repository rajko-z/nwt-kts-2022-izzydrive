package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
