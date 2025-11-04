package com.au.sb.ticket.system.api.config;

import com.au.sb.ticket.system.api.constants.ApiConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security config class to allow required URL only.
 * It will
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Allow access to actuator endpoints
                                .requestMatchers(ApiConstant.ACTUATOR_ENDPOINTS).permitAll()

                                // Allow access to the specific API URL
                                .requestMatchers(ApiConstant.TRANSACTION_ENDPOINT).permitAll()

                                .anyRequest().authenticated()
                );

        return http.build();
    }
}
