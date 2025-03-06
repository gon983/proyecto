package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.dtos.create.UserStateCreateDTO;
import com.proyect.mvp.domain.model.entities.UserStateEntity;
import com.proyect.mvp.domain.repository.UserStateRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserStateService {

    private final UserStateRepository userStateRepository;

    public UserStateService(UserStateRepository userStateRepository) {
        this.userStateRepository = userStateRepository;
    }

    public Flux<UserStateEntity> getAllUserStates() {
        return userStateRepository.findAll();
    }

    public Mono<UserStateEntity> getUserStateById(UUID id) {
        return userStateRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<UserStateEntity> saveNewUserState(UserStateCreateDTO userState) {
        UserStateEntity userStateEntity = UserStateEntity.builder()
                .idUserState(UUID.randomUUID())
                .name(userState.getName())
                .build();

        return userStateRepository.insertUserState(userStateEntity.getIdUserState(), userStateEntity.getName())
                .thenReturn(userStateEntity)
                .onErrorMap(error -> {
                    System.err.println("Error al guardar estado de usuario: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving user state", error);
                });
    }


    public Mono<Void> deleteUserStateById(UUID id) {
        return userStateRepository.findById(id)
                .flatMap(existingUserState -> userStateRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
