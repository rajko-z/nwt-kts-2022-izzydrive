package com.izzydrive.backend.repository.users;

import com.izzydrive.backend.model.users.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {

    @Query("select u from MyUser u where u.email=?1 and u.activated = true and u.blocked = false")
    Optional<MyUser> findByEmail(String email);
}
