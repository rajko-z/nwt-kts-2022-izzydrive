package com.izzydrive.backend.repository.users;

import com.izzydrive.backend.model.users.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
