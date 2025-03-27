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
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

import com.proyect.mvp.infrastructure.security.handlers.JsonAccessDeniedHandler;
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
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .authenticationManager(authenticationManager)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/public/**").permitAll()
                .pathMatchers("/login").permitAll()
                .pathMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                .pathMatchers("/api/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_CP_OWNER", "ROLE_PRODUCTOR")
                .pathMatchers("/api/cp_owner/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CP_OWNER")
                .pathMatchers("/api/productor/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PRODUCTOR")
                .anyExchange().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling(handling -> handling
            .accessDeniedHandler(new JsonAccessDeniedHandler())
            .authenticationEntryPoint(new JsonAuthenticationEntryPoint())
            
        )
        .build();

    
}}
