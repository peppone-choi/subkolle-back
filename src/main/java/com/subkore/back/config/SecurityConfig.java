package com.subkore.back.config;

import com.subkore.back.auth.jwt.JwtAuthenticationFilter;
import com.subkore.back.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request ->
                request
                    .requestMatchers(PathRequest.toH2Console()).permitAll()
                    .requestMatchers("api/v1/menus/**").permitAll()
                    .requestMatchers("api/v1/carousels/**").permitAll()
                    .requestMatchers("api/v1/events/**").permitAll()
                    .requestMatchers("api/v1/users/**").permitAll()
                    .anyRequest().authenticated()).csrf(csrf -> csrf.ignoringRequestMatchers("api/v1"
                + "/**").ignoringRequestMatchers(PathRequest.toH2Console()))
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000", "http://localhost:8080")
                    .allowedMethods("GET", "POST", "PUT", "DELETE").allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }
}
