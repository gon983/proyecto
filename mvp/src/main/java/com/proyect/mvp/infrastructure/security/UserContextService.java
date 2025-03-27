package com.proyect.mvp.infrastructure.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

@Service
public class UserContextService {
    
    public Mono<String> getCurrentIdUser() {
        return ReactiveSecurityContextHolder.getContext()
            .map(securityContext -> {
                Authentication authentication = securityContext.getAuthentication();
                return authentication != null ? authentication.getName() : null;
            });
    }
    
   
    
    public Mono<List<String>> getCurrentUserRoles() {
        return ReactiveSecurityContextHolder.getContext()
            .map(securityContext -> {
                Authentication authentication = securityContext.getAuthentication();
                return authentication != null 
                    ? authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
                    : Collections.emptyList();
            });
    }
}
