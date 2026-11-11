
package com.healthcare.patient.config;

import com.healthcare.patient.security.JwtAuthenticationFilter;
import com.healthcare.patient.security.SecurityErrorHandler;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityErrorHandler securityErrorHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // Disable CSRF for stateless REST APIs
                .csrf(csrf -> csrf.disable())

                // Configure API authorization
                .authorizeHttpRequests(auth -> auth

                        // Public health check
                        .requestMatchers("/actuator/health").permitAll()

                        // All other APIs require authentication
                        .anyRequest().authenticated()
                )

                // Custom 401 JSON error response
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(securityErrorHandler)
                )

                // No HTTP session
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Add JWT filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
