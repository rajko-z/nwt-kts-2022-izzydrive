package com.izzydrive.backend.repository.users;

import com.izzydrive.backend.model.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
