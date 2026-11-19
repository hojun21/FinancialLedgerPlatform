package com.example.financialledgersystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // allow accessing /signup and /error without authentication
        http.authorizeHttpRequests(
                auth ->
                        auth.requestMatchers("/signup", "/error").permitAll()
                                .anyRequest().authenticated()
        ).formLogin(Customizer.withDefaults())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/signup"));

        // build SecurityFilterChain object and return
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ // hashing password
        return new BCryptPasswordEncoder();
    }

}
