package com.proyect.mvp.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.proyect.mvp.domain.repository.UserRepository;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private static final Logger log = LoggerFactory.getLogger(CustomReactiveAuthenticationManager.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public CustomReactiveAuthenticationManager(
        UserRepository userRepository, 
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
    log.debug("Iniciando autenticación para: {}", authentication.getName());
    
    // Autenticación JWT (sin credenciales de password)
    if (authentication.getCredentials() == null || 
        authentication.getCredentials() instanceof String) {  // Modificado aquí
        log.debug("Autenticación JWT detectada");
        String username;
        
        if (authentication.getPrincipal() instanceof UserAuthenticationDTO) {
            username = ((UserAuthenticationDTO) authentication.getPrincipal()).getUsername();
            log.debug("Extraído username de UserAuthenticationDTO: {}", username);
        } else {
            username = authentication.getName();
            log.debug("Username obtenido directamente: {}", username);
        }
        
        return userRepository.findByUsername(username)
            .doOnNext(user -> log.debug("Usuario encontrado en DB: {}", user.getUsername()))
            .switchIfEmpty(Mono.defer(() -> {
                log.error("Usuario no encontrado: {}", username);
                return Mono.error(new UsernameNotFoundException("Usuario no encontrado: " + username));
            }))
            .map(user -> {
                List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getRole().split(","))
                    .map(String::trim)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
                
                log.debug("Roles asignados: {}", authorities);
                return (Authentication) new UsernamePasswordAuthenticationToken(
                    user, 
                    null,  // Credentials a null para JWT
                    authorities
                );
            });
    }
    
    log.debug("Autenticación estándar (login) detectada");
    return standardAuthenticate(authentication);
}

    private Mono<Authentication> standardAuthenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.debug("Autenticando usuario estándar: {}", username);

        return userRepository.findByUsername(username)
            .flatMap(user -> {
                log.debug("Comparando contraseñas para usuario: {}", username);
                if (passwordEncoder.matches(password, user.getPassword())) {
                    List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getRole().split(","))
                        .map(String::trim)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                    
                    log.debug("Autenticación exitosa para: {}", username);
                    return Mono.just((Authentication) new UsernamePasswordAuthenticationToken(
                        user, 
                        authentication.getCredentials(),
                        authorities
                    ));
                } else {
                    log.error("Credenciales inválidas para usuario: {}", username);
                    return Mono.error(new BadCredentialsException("Credenciales inválidas"));
                }
            })
            .switchIfEmpty(Mono.defer(() -> {
                log.error("Usuario no encontrado en DB: {}", username);
                return Mono.error(new UsernameNotFoundException("Usuario no encontrado"));
            }));
    }
}