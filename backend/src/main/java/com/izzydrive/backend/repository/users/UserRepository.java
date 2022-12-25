package com.izzydrive.backend.repository.users;

import com.izzydrive.backend.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email=?1 and u.activated = true")
    Optional<User> findByEmail(String email);

}
