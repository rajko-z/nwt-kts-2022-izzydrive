package com.izzydrive.backend.service.users;

import com.izzydrive.backend.model.users.MyUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    List<MyUser> findAll();

    Optional<MyUser> findByEmail(String email);
    void processOAuthPostLogin(String username);

}
