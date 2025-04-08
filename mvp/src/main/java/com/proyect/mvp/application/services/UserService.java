package com.proyect.mvp.application.services;

import com.proyect.mvp.application.dtos.create.UserCreateDTO;
import com.proyect.mvp.application.dtos.update.UserUpdateDTO;
import com.proyect.mvp.domain.model.entities.UserEntity;
import com.proyect.mvp.domain.repository.UserRepository;
import com.proyect.mvp.infrastructure.security.UserAuthenticationDTO;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final NeighborhoodService neighborhoodService;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, NeighborhoodService neighborhoodService, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.neighborhoodService = neighborhoodService;
    }

    public Flux<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<UserEntity> getUserById(UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

   
  

    

   

   

    

    public Mono<UserEntity> register(UserAuthenticationDTO dto){
        UserEntity user = UserEntity.builder()
                            .username(dto.getUsername())
                            .email(dto.getEmail())
                            .role("ROLE_USER")        
                            .password(dto.getPassword())
                            .createdAt(LocalDateTime.now())
                            .build();
        return userRepository.save(user);
    }



    
}

