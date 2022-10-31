package com.izzydrive.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {

    @Value("${security.jwt-token.secret}")
    private String secret;

    @Value("${security.jwt-token.expires}")
    private Long expires;

    @Bean
    public String jwtSecret() {
        return secret;
    }

    @Bean
    public Long jwtExpiresIn() {
        return expires;
    }
}
