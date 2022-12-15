package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.model.users.MyUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    List<MyUser> findAll();

    Optional<MyUser> findByEmail(String email);
    void processOAuthPostLogin(String username);

    String generatePassword();

    String getProfileImage(Long userId);



}
