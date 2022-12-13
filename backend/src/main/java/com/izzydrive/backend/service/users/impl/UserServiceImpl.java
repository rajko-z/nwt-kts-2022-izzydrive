package com.izzydrive.backend.service.users.impl;

import com.izzydrive.backend.dto.NewPasswordDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.users.UserRepository;
import com.izzydrive.backend.service.Validator;
import com.izzydrive.backend.service.users.UserService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent())
            return user.get();
        throw new UsernameNotFoundException("User with username: " + username + " not found");
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void processOAuthPostLogin(String username) {
        Optional<User> existUser = userRepository.findByEmail(username);

    }

    @Override
    public void changePassword(NewPasswordDTO newPasswordDTO) {
        User user = userRepository.findByEmail(newPasswordDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(newPasswordDTO.getEmail())));

        if (passwordsMatch(newPasswordDTO.getNewPassword(), user.getPassword())) {
            throw new BadRequestException(ExceptionMessageConstants.NEW_PASSWORD_SAME_AS_PREVIOUS);
        }
        if (!passwordsMatch(newPasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException(ExceptionMessageConstants.INVALID_CURRENT_PASSWORD);
        }
        if (Validator.validatePassword(newPasswordDTO.getNewPassword())) {
            user.setPassword(passwordEncoder.encode(newPasswordDTO.getNewPassword()));
            userRepository.save(user);
        }
    }

    private boolean passwordsMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
