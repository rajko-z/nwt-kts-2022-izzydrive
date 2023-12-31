package com.izzydrive.backend.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.izzydrive.backend.converters.UserDTOConverter;
import com.izzydrive.backend.dto.LoginDTO;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.exception.InvalidCredentialsException;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.service.AuthenticationService;
import com.izzydrive.backend.service.ImageService;
import com.izzydrive.backend.service.users.UserService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import com.izzydrive.backend.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private final TokenUtils tokenUtils;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final ImageService imageService;

    @Value("${google.id}")
    private String idClient;

    @Transactional
    public UserWithTokenDTO createAuthenticationToken(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            checkIfUserBlocked(user);
            String jwt = tokenUtils.generateTokenForUsername(user.getUsername());
            return UserDTOConverter.convertToUserWithImageAndToken(user, jwt, imageService);
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException(ExceptionMessageConstants.INVALID_LOGIN);
        }
    }

    public UserWithTokenDTO createAuthenticationTokenFBLogin(@RequestBody String token) {
        Facebook facebook = new FacebookTemplate(token);
        String [] data = {"email"};
        org.springframework.social.facebook.api.User user = facebook.fetchObject("me", org.springframework.social.facebook.api.User.class,data);

        Optional<User> myUser = userService.findByEmail(user.getEmail());
        myUser.ifPresent(this::checkIfUserBlocked);
        // TODO Da li ce uvek postojati myUser.get(), mozda i ovde da se baci InvalidCredentialsException
        String jwt = tokenUtils.generateTokenForUsername(myUser.get().getUsername());
        return UserDTOConverter.convertToUserWithImageAndToken(myUser.get(), jwt, imageService);
    }

    public UserWithTokenDTO createAuthenticationTokenGoogleLogin(String token) {
        try {
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


            Optional<User> myUser =  userService.findByEmail(payload.getEmail());
            myUser.ifPresent(this::checkIfUserBlocked);
            //TODO Da li ce uvek postojati myUser.get(), mozda i ovde da se baci InvalidCredentialsException
            String jwt = tokenUtils.generateTokenForUsername(myUser.get().getUsername());
            return UserDTOConverter.convertToUserWithImageAndToken(myUser.get(), jwt, imageService);
        } catch (IOException ex) {
            throw new InvalidCredentialsException(ExceptionMessageConstants.INVALID_LOGIN);
        }
    }

    private void checkIfUserBlocked(User user){
        if(user.isBlocked())
            throw new InvalidCredentialsException(ExceptionMessageConstants.USER_IS_BLOCK_MESSAGE);
    }
}
