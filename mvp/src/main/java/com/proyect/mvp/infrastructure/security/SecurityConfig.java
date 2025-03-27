package com.proyect.mvp.infrastructure.security;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.proyect.mvp.infrastructure.security.handlers.JsonAuthenticationEntryPoint;
import com.proyect.mvp.infrastructure.security.handlers.JsonAuthenticationFailureHandler;
import com.proyect.mvp.infrastructure.security.handlers.JsonAuthenticationSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final CustomReactiveAuthenticationManager authenticationManager;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomReactiveAuthenticationManager authenticationManager, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationManager = authenticationManager;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(csrf -> csrf.disable())
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.HTTP_BASIC)
            .authenticationManager(authenticationManager)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/public/**").permitAll()
                .pathMatchers("/login").permitAll()
                .pathMatchers("/api/admin/**").hasRole("ADMIN")
                .pathMatchers("/api/consumidor/**").hasRole("CONSUMIDOR")
                .pathMatchers("/api/cp_owner/**").hasRole("CP_OWNER")
                .pathMatchers("/api/productor/**").hasRole("PRODUCTOR")
                .anyExchange().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling(handling -> handling.authenticationEntryPoint(new JsonAuthenticationEntryPoint())
        )
        .build();

    
}}
