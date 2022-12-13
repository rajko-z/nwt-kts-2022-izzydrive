package com.izzydrive.backend.config;

import com.izzydrive.backend.service.impl.CustomOAuth2UserService;
import com.izzydrive.backend.service.security.RestAuthenticationEntryPoint;
import com.izzydrive.backend.service.security.TokenAuthenticationFilter;
import com.izzydrive.backend.service.users.UserService;
import com.izzydrive.backend.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private final TokenUtils tokenUtils;

    private final CustomOAuth2UserService oauth2UserService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()

            .authorizeRequests()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/users/**").permitAll()
            .antMatchers("/admins/**").permitAll()
            .antMatchers("/passengers/**").permitAll()
            .antMatchers("/drivers/**").permitAll()
            .antMatchers("/addresses/**").permitAll()
            .antMatchers("/admin-notes/**").permitAll()
            .antMatchers("/drivings/**").permitAll()
            .antMatchers("/driving-notes/**").permitAll()
            .antMatchers("/evaluations/**").permitAll()
            .antMatchers("/images/**").permitAll()
            .antMatchers("/messages/**").permitAll()
            .antMatchers("/routes/**").permitAll()
            .antMatchers("/working-intervals/**").permitAll()

            .anyRequest().authenticated().and()
            .cors().and()
            .addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userService), BasicAuthenticationFilter.class)
            .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauth2UserService)
                .and()
                .and();
        http.csrf().disable();
    }

    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.POST, "/auth/login", "/auth/login-fb","/passengers/registration");
        web.ignoring().antMatchers(HttpMethod.GET,  "/","/webjars/**", "/*.html", "favicon.ico", "/**/*.html",
                "/**/*.css", "/**/*.js");
        web.ignoring().antMatchers(HttpMethod.GET,"/confirmation");
    }
}
