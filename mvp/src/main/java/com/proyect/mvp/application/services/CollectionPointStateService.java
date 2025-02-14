package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.CollectionPointStateEntity;
import com.proyect.mvp.domain.repository.CollectionPointStateRepository;
import com.proyect.mvp.dtos.create.CollectionPointStateCreateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CollectionPointStateService {

    private final CollectionPointStateRepository collectionPointStateRepository;

    public CollectionPointStateService(CollectionPointStateRepository collectionPointStateRepository) {
        this.collectionPointStateRepository = collectionPointStateRepository;
    }

    public Flux<CollectionPointStateEntity> getAllCollectionPointStates() {
        return collectionPointStateRepository.findAll();
    }

    public Mono<CollectionPointStateEntity> getCollectionPointStateById(UUID id) {
        return collectionPointStateRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<CollectionPointStateEntity> saveNewCollectionPointState(CollectionPointStateCreateDTO collectionPointState) {
        CollectionPointStateEntity collectionPointStateEntity = CollectionPointStateEntity.builder()
                .idCollectionPointState(UUID.randomUUID())
                .name(collectionPointState.getName())
                .build();

        return collectionPointStateRepository.insertCollectionPointState(collectionPointStateEntity.getIdCollectionPointState(), collectionPointStateEntity.getName())
                .thenReturn(collectionPointStateEntity)
                .onErrorMap(error -> {
                    System.err.println("Error al guardar estado de punto de recolección: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving collection point state", error);
                });
    }


    public Mono<Void> deleteCollectionPointStateById(UUID id) {
        return collectionPointStateRepository.findById(id)
                .flatMap(existingCollectionPointState -> collectionPointStateRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}