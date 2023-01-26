package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.LoginDTO;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.service.AuthenticationService;
import com.izzydrive.backend.service.users.driver.workingtime.WorkingIntervalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final WorkingIntervalService workingIntervalService;

    @PostMapping("/login")
    public ResponseEntity<UserWithTokenDTO> createAuthenticationToken(@RequestBody LoginDTO loginDTO) {
        UserWithTokenDTO userWithToken = authenticationService.createAuthenticationToken(loginDTO);
        if (userWithToken.getUser().getRole().equals("ROLE_DRIVER")) {
            workingIntervalService.setCurrentLoggedDriverStatusToActive();
        }
        return new ResponseEntity<>(userWithToken, HttpStatus.OK);
    }

    @PostMapping("/login-fb")
    public ResponseEntity<Object> createAuthenticationTokenFBLogin(@RequestBody String token) {
        UserWithTokenDTO userWithToken = authenticationService.createAuthenticationTokenFBLogin(token);
        if (userWithToken.getUser().getRole().equals("ROLE_DRIVER")) {
            workingIntervalService.setCurrentLoggedDriverStatusToActive();
        }
        return new ResponseEntity<>(userWithToken, HttpStatus.OK);
    }

    @PostMapping("/login-google")
    public ResponseEntity<Object> createAuthenticationTokenGoogleLogin(@RequestBody String token) {
        UserWithTokenDTO userWithToken = authenticationService.createAuthenticationTokenGoogleLogin(token);
        if (userWithToken.getUser().getRole().equals("ROLE_DRIVER")) {
            workingIntervalService.setCurrentLoggedDriverStatusToActive();
        }
        return new ResponseEntity<>(userWithToken, HttpStatus.OK);
    }
}
