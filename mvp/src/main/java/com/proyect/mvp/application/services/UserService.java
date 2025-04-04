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

    public Mono<UserEntity> saveNewUser(UserCreateDTO userDto) {
        UserEntity userEntity = UserEntity.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .createdAt(LocalDateTime.now())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .documentType(userDto.getDocumentType())
                .documentNumber(userDto.getDocumentNumber())
                .fkNeighborhood(userDto.getFkNeighborhood())
                .phone(userDto.getPhone())
                .role(userDto.getRole())
            
                .build();
        return userRepository.save(userEntity)
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
                            .role(userDto.getRole())
                            .build();
                    

                    return userRepository.save(userEntity); // Retornar el nuevo usuario guardado
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<UUID> getLocality(UUID idUser){
        return getUserById(idUser)
                .flatMap(user -> neighborhoodService.getNeighborhoodById(user.getFkNeighborhood()))
                .flatMap(neighborhood -> Mono.just(neighborhood.getFkLocality()));
    }

    

   

    public Mono<String> getMpProductorUserId(UUID productorId) {
        return userRepository.findById(productorId)
                            .flatMap(productor-> Mono.just(productor.getUserProductorMpId()));
    }

    public Mono<String> getMpRefreshToken(UUID productorId) {
        return userRepository.findById(productorId)
                            .flatMap(productor-> Mono.just(productor.getRefreshProductorToken()));
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

