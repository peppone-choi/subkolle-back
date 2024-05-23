package com.subkore.back.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request ->
            request
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers("api/v1/menus/**").permitAll()
                .anyRequest().authenticated()).csrf(csrf -> csrf.ignoringRequestMatchers("api/v1"
                + "/**"))
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));

        return http.build();
    }
}
