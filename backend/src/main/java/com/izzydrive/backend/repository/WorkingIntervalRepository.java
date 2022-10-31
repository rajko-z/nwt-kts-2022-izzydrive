package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.WorkingInterval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingIntervalRepository extends JpaRepository<WorkingInterval, Long> {
}
