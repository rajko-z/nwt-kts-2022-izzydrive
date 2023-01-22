package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByRegistration(String registration);

    @Query("select c from Car c where c.driver.id = ?1")
    Optional<Car> findByUserId(Long id);
}

