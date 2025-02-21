package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.UserEntity;
import com.proyect.mvp.domain.repository.UserRepository;
import com.proyect.mvp.dtos.create.UserCreateDTO;
import com.proyect.mvp.dtos.update.UserUpdateDTO;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<UserEntity> getUserById(UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<UserEntity> saveNewUser(UserCreateDTO userDto) {
        UserEntity userEntity = UserEntity.builder()
                .idUser(UUID.randomUUID())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .createdAt(LocalDateTime.now())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .documentType(userDto.getDocumentType())
                .documentNumber(userDto.getDocumentNumber())
                .fkNeighborhood(userDto.getFkNeighborhood())
                .phone(userDto.getPhone())
                .roleOne(userDto.getRoleOne())
                .fkCollectionPointSuscribed(userDto.getFkCollectionPointSuscribed())
                .build();
        return userRepository.insertUser(userEntity)
                .thenReturn(userEntity)
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving user", error));
    }

    public Mono<UserEntity> updateUser(UUID id, UserUpdateDTO userDto) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    UserEntity userEntity = UserEntity.builder()
                            .idUser(id)  // Mantener el mismo ID
                            .username(userDto.getUsername())
                            .email(userDto.getEmail())
                            .createdAt(existingUser.getCreatedAt()) // Mantener la fecha original
                            .firstName(userDto.getFirstName())
                            .lastName(userDto.getLastName())
                            .documentType(userDto.getDocumentType())
                            .documentNumber(userDto.getDocumentNumber())
                            .fkNeighborhood(userDto.getFkNeighborhood())
                            .phone(userDto.getPhone())
                            .roleOne(userDto.getRoleOne())
                            .fkCollectionPointSuscribed(userDto.getFkCollectionPointSuscribed())
                            .build();
                    
                    return userRepository.save(userEntity); // Retornar el nuevo usuario guardado
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
    
}

