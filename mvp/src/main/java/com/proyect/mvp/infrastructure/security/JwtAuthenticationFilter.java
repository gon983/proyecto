package com.proyect.mvp.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class JwtAuthenticationFilter implements WebFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final ReactiveAuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(JwtService jwtService, ReactiveAuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    
    @Override
public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    log.debug("Iniciando filtro JWT para ruta: {}", exchange.getRequest().getPath());
    
    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        log.debug("Token JWT encontrado: {}", token);
        
        if (jwtService.validateToken(token)) {
            String username = jwtService.extractUsername(token);
            log.debug("Token válido para usuario: {}", username);
            
            // Crear autenticación sin credentials (o con el token como credentials)
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                    username, 
                    null,  // Credentials a null
                    null   // Authorities se establecerán después
                );
            
            return authenticationManager.authenticate(authentication)
                .flatMap(auth -> {
                    log.debug("Autenticación exitosa, estableciendo contexto para: {}", username);
                    return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                })
                .onErrorResume(ex -> {
                    log.error("Error en autenticación JWT: {}", ex.getMessage());
                    return Mono.error(ex);  // Propagar el error en lugar de continuar
                });
        } else {
            log.error("Token JWT inválido");
            return Mono.error(new BadCredentialsException("Token inválido"));
        }
    }
    
    log.debug("No se encontró header Authorization válido");
    return chain.filter(exchange);
}}