package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
