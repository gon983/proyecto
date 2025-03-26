package com.proyect.mvp.infrastructure.security;



import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.proyect.mvp.domain.repository.UserRepository;

import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {
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
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        return userRepository.findByUsername(username)
            .flatMap(user -> {
                if (passwordEncoder.matches(password, user.getPassword())) {
                    var authorities = Arrays.stream(user.getRol().split(","))
                                            .map(String::trim)
                                            .map(SimpleGrantedAuthority::new)
                                            .collect(Collectors.toList());
                    
                    return Mono.just((Authentication) new UsernamePasswordAuthenticationToken(
                        user, 
                        password, 
                        authorities
                    ));
                }
                return Mono.empty();
            })
            .switchIfEmpty(Mono.error(new Exception("Usuario no encontrado")));
    }
}