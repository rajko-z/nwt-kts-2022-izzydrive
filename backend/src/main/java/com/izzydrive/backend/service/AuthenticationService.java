package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.LoginDTO;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationService {

    UserWithTokenDTO createAuthenticationToken(LoginDTO loginDTO);

    UserWithTokenDTO createAuthenticationTokenFBLogin(@RequestBody String token);

    UserWithTokenDTO createAuthenticationTokenGoogleLogin(String token);
}
