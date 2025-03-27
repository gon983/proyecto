package com.proyect.mvp.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class UserContextService {
    private static final Logger log = LoggerFactory.getLogger(UserContextService.class);
    
    public Mono<String> getCurrentIdUser() {
        return ReactiveSecurityContextHolder.getContext()
            .map(securityContext -> {
                Authentication authentication = securityContext.getAuthentication();
                if (authentication != null) {
                    log.debug("Obteniendo current user: {}", authentication.getName());
                    return authentication.getName();
                }
                log.warn("No se encontró autenticación en el contexto");
                return null;
            });
    }
    
    public Mono<List<String>> getCurrentUserRoles() {
        return ReactiveSecurityContextHolder.getContext()
            .map(securityContext -> {
                Authentication authentication = securityContext.getAuthentication();
                if (authentication != null) {
                    List<String> roles = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
                    log.debug("Roles obtenidos: {}", roles);
                    return roles;
                }
                log.warn("No se encontró autenticación para obtener roles");
                return Collections.emptyList();
            });
    }
}