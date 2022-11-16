package com.izzydrive.backend.controller;

import com.izzydrive.backend.converters.UserDTOConverter;
import com.izzydrive.backend.dto.LoginDTO;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final TokenUtils tokenUtils;

    private final AuthenticationManager authenticationManager;

    private static final String ERROR_INVALID_LOGIN = "Invalid username or password";

    @PostMapping("/login")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            String jwt = tokenUtils.generateTokenForUsername(user.getUsername());

            return new ResponseEntity<>(UserDTOConverter.convertToUserWithToken(user,jwt), HttpStatus.OK);
        }
        catch (BadCredentialsException ex) {
            return new ResponseEntity<>(ERROR_INVALID_LOGIN, HttpStatus.UNAUTHORIZED);
        }
    }
}
