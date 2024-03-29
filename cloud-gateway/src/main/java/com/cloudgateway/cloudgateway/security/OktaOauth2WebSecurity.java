package com.cloudgateway.cloudgateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class OktaOauth2WebSecurity {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((req) -> req.anyRequest().authenticated())
                .oauth2ResourceServer((srv) -> srv.jwt(Customizer.withDefaults()))
                .build();
    }
}
