package com.proyect.mvp.infrastructure.security;


import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class UserContextHolder {
    public static Mono<UUID> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
            .map(context -> {
                Object principal = context.getAuthentication().getPrincipal();
                if (principal instanceof UserDetails) {
                    // Asume que tienes un método getId() en tu UserDTO
                    return ((UserDTO) principal).getId();
                }
                return null;
            });
    }
}
