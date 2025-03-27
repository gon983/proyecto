package com.proyect.mvp.infrastructure.security;



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
    if (authentication == null) {
        return Mono.error(new BadCredentialsException("Authentication cannot be null"));
    }
    
    String username = authentication.getName();
    System.out.println("UserName: " + username);
    Object credentials = authentication.getCredentials();
    
    if (credentials == null) {
        return Mono.error(new BadCredentialsException("Credentials cannot be null"));
    }
    
    String password = credentials.toString();

    return userRepository.findByUsername(username)
          .doOnNext(user -> {
        System.out.println("Found user: " + user.getUsername());
        System.out.println("Stored password: " + user.getPassword());
        System.out.println("Entered password: " + password);
        System.out.println("Password matches: " + passwordEncoder.matches(password, user.getPassword()));
    })
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
        .flatMap(user -> {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return Mono.error(new BadCredentialsException("Invalid credentials"));
            }
            
            List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getRole().split(","))
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
            
            return Mono.just(new UsernamePasswordAuthenticationToken(
                user, 
                null, // Don't store password after authentication
                authorities
            ));
        });
}
}