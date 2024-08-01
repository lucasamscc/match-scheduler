package com.project.matchscheduler.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();


    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                // Enable CSRF protection
                .authorizeRequests(auth -> auth // Use 'auth' instead of deprecated 'authorizeRequests()'
                        .dispatcherTypeMatchers(HttpMethod.POST, DispatcherType.valueOf("/api/public/**")).permitAll() // Allow public GET requests
                        .dispatcherTypeMatchers(HttpMethod.valueOf("/api/users/admin/**")).hasRole("ADMIN")
                        .anyRequest().authenticated()) // Chain authorization rules
                .httpBasic(customizer -> customizer.realmName("My App Realm")); // Configure HTTP Basic authentication
    }
}
