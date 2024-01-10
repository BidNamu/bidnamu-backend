package com.bidnamu.bidnamubackend.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .addFilterBefore(corsFilter, CorsFilter.class)
            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(e -> {
                    e.accessDeniedHandler(accessDeniedHandler);
                    e.authenticationEntryPoint(authenticationEntryPoint);
                }
            )
            .sessionManagement(e -> e.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(registry -> registry
                .requestMatchers(HttpMethod.PATCH, "/users/{email}/status").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/auctions").hasRole("SELLER")
                .requestMatchers(HttpMethod.PATCH, "/users/status").hasRole("USER")
                .requestMatchers("/", "/**").permitAll()
            )
            .getOrBuild();
    }
}
