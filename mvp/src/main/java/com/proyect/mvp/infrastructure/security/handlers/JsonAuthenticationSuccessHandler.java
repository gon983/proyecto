package com.proyect.mvp.infrastructure.security.handlers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyect.mvp.infrastructure.security.JwtService;
import com.proyect.mvp.infrastructure.security.UserAuthenticationDTO;

import org.codehaus.plexus.component.annotations.Component;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class JsonAuthenticationSuccessHandler {
    private JwtService jwtService;

    public JsonAuthenticationSuccessHandler(JwtService jwtService){
        this.jwtService = jwtService;
    }
    
    public Mono<ServerResponse> createResponse(Authentication authentication) {
        String token = jwtService.generateToken(authentication);
        UserAuthenticationDTO principal = (UserAuthenticationDTO) authentication.getPrincipal();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", "success");
        responseBody.put("message", "Authentication successful");
        responseBody.put("userId", principal.getIdUser().toString());
        responseBody.put("token", token);
        responseBody.put("roles", authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
        
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(responseBody);
    }
}