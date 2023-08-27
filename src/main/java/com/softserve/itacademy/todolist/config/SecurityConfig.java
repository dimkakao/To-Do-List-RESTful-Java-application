package com.softserve.itacademy.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .headers(h -> h
                        .frameOptions() // for Postman, the H2 console
                        .disable())
                .csrf().disable()
                .authorizeHttpRequests(req -> req
                        .anyRequest().permitAll());
        return http.build();
    }

}
