package com.izzydrive.backend.utils;

import com.izzydrive.backend.config.JWTConfig;
import com.izzydrive.backend.model.users.MyUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@AllArgsConstructor
public class TokenUtils {

    private final static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private static final String AUTH_HEADER = "Authorization";

    private static final String BEARER_PREFIX = "Bearer ";

    private final JWTConfig jwtConfig;

    public String generateTokenForUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, jwtConfig.jwtSecret()).compact();
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        MyUser myUser = (MyUser) userDetails;
        String username = getUsernameFromToken(token);
        return username != null && userDetails.getUsername().equals(username);
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims =  Jwts.parser()
                    .setSigningKey(jwtConfig.jwtSecret())
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + jwtConfig.jwtExpiresIn());
    }

}
