package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.NewPasswordDTO;
import com.izzydrive.backend.model.users.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    List<User> findAll();

    Optional<User> findByEmail(String email);
    void processOAuthPostLogin(String username);

    void changePassword(NewPasswordDTO newPasswordDTO);
}
