package com.izzydrive.backend.repository.users;

import com.izzydrive.backend.model.users.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
