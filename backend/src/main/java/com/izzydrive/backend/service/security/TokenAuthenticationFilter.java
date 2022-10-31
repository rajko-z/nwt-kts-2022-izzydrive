package com.izzydrive.backend.service.security;

import com.izzydrive.backend.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenUtils.getToken(request);

        if (token != null) {

            String username = tokenUtils.getUsernameFromToken(token);

            if (username != null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (tokenUtils.validateToken(token, userDetails)) {

                    TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                    authentication.setToken(authentication.getToken());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
