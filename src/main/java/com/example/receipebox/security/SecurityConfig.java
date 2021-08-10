package com.example.receipebox.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.http.HttpMethod.*;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String ADMIN = "ADMIN";
    private final String COOK = "COOK";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable()
                .authorizeRequests()
                .mvcMatchers(POST, "/recipe/public").hasAuthority(ADMIN)
                .mvcMatchers(GET, "/recipe/public").hasAuthority(ADMIN)
                .mvcMatchers(POST, "/recipe/private").hasAuthority(COOK)
                .mvcMatchers(GET, "/recipe/private").hasAuthority(COOK)
                .mvcMatchers(POST, "/recipe/{id}").hasAuthority(ADMIN)
                .mvcMatchers(POST, "/recipe/update/{id}").hasAuthority(ADMIN)
                .mvcMatchers(DELETE, "/recipe/{id}").hasAuthority(ADMIN)
                .mvcMatchers(GET, "/recipe/{recipeName}").authenticated()
                .mvcMatchers(POST, "/ingredient").hasAuthority(ADMIN)
                .mvcMatchers(PATCH, "/ingredient").hasAuthority(ADMIN)
                .mvcMatchers(POST, "/user").permitAll()
                .anyRequest().permitAll();
    }
}
