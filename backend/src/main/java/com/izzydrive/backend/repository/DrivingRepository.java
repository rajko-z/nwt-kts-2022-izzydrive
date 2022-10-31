package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.Driving;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingRepository extends JpaRepository<Driving, Long> {
}
