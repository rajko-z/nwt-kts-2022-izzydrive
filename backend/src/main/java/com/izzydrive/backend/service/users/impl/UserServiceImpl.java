package com.izzydrive.backend.service.users.impl;

import com.izzydrive.backend.model.users.MyUser;
import com.izzydrive.backend.repository.users.UserRepository;
import com.izzydrive.backend.service.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = userRepository.findByEmail(username);
        if (user.isPresent())
            return user.get();
        throw new UsernameNotFoundException("User with username: " + username + " not found");
    }

    @Override
    public List<MyUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<MyUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void processOAuthPostLogin(String username) {
        Optional<MyUser> existUser = userRepository.findByEmail(username);

    }

}
