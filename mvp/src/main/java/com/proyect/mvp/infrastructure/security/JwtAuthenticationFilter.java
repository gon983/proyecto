package com.proyect.mvp.infrastructure.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Configuration
public class JwtAuthenticationFilter implements WebFilter {
    private final JwtService jwtService;
    private final ReactiveAuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(JwtService jwtService, ReactiveAuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtService.validateToken(token)) {
                String username = jwtService.extractUsername(token);
                // Create authentication based on the token
                return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, null)
                ).flatMap(authentication -> {
                    exchange.getRequest().mutate().header("X-Authorization", authentication.getName());
                    return chain.filter(exchange);
                });
            }
        }
        
        return chain.filter(exchange);
    }
}

