package com.izzydrive.backend.controller;

import com.izzydrive.backend.converters.UserDTOConverter;
import com.izzydrive.backend.dto.LoginDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.model.users.MyUser;
import com.izzydrive.backend.service.users.UserService;
import com.izzydrive.backend.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final TokenUtils tokenUtils;

    private final AuthenticationManager authenticationManager;

    private UserService userService;

    private static final String ERROR_INVALID_LOGIN = "Invalid username or password";

    @Value("${google.id}")
    private String idClient;

    @PostMapping("/login")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody LoginDTO loginDTO) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            MyUser myUser = (MyUser) authentication.getPrincipal();
            String jwt = tokenUtils.generateTokenForUsername(myUser.getUsername());

            return new ResponseEntity<>(UserDTOConverter.convertToUserWithToken(myUser,jwt), HttpStatus.OK);
        }
        catch (BadCredentialsException ex) {
            return new ResponseEntity<>(ERROR_INVALID_LOGIN, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login-fb")
    public ResponseEntity<Object> createAuthenticationTokenFBLogin(@RequestBody String token) {
        try {

            Facebook facebook = new FacebookTemplate(token);
            String [] data = {"email"};
            User user = facebook.fetchObject("me", User.class,data);

            Optional<MyUser> myUser = userService.findByEmail(user.getEmail());
            String jwt = tokenUtils.generateTokenForUsername(myUser.get().getUsername());

            return new ResponseEntity<>(UserDTOConverter.convertToUserWithToken(myUser.get(),jwt), HttpStatus.OK);

        }
        catch (BadCredentialsException ex) {
            return new ResponseEntity<>(ERROR_INVALID_LOGIN, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login-google")
    public ResponseEntity<Object> createAuthenticationTokenGoogleLogin(@RequestBody String token) {
        try {
            System.out.println(token);
            NetHttpTransport transport = new NetHttpTransport();
            GsonFactory factory = GsonFactory.getDefaultInstance();
            GoogleIdTokenVerifier.Builder tokenVerifier =
                    new GoogleIdTokenVerifier.Builder(transport,factory)
                            .setAudience(Collections.singleton(idClient));

            GoogleIdToken googleIdToken = GoogleIdToken.parse(
                    tokenVerifier.getJsonFactory(),
                    token
            );
            GoogleIdToken.Payload payload = googleIdToken.getPayload();


            Optional<MyUser> myUser =  userService.findByEmail(payload.getEmail());
            String jwt = tokenUtils.generateTokenForUsername(myUser.get().getUsername());

            return new ResponseEntity<>(UserDTOConverter.convertToUserWithToken(myUser.get(),jwt), HttpStatus.OK);


        }
        catch (BadCredentialsException | IOException ex) {
            return new ResponseEntity<>(ERROR_INVALID_LOGIN, HttpStatus.UNAUTHORIZED);
        }
    }
}
