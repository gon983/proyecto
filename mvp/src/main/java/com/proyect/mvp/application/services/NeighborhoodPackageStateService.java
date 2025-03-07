package com.proyect.mvp.application.services;

import com.proyect.mvp.application.dtos.create.NeighborhoodPackageStateCreateDTO;
import com.proyect.mvp.domain.model.entities.NeighborhoodPackageStateEntity;
import com.proyect.mvp.domain.repository.NeighborhoodPackageStateRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class NeighborhoodPackageStateService {

    private final NeighborhoodPackageStateRepository neighborhoodPackageStateRepository;

    public NeighborhoodPackageStateService(NeighborhoodPackageStateRepository neighborhoodPackageStateRepository) {
        this.neighborhoodPackageStateRepository = neighborhoodPackageStateRepository;
    }

    public Flux<NeighborhoodPackageStateEntity> getAllNeighborhoodPackageStates() {
        return neighborhoodPackageStateRepository.findAll();
    }

    public Mono<NeighborhoodPackageStateEntity> getNeighborhoodPackageStateById(UUID id) {
        return neighborhoodPackageStateRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<NeighborhoodPackageStateEntity> saveNewNeighborhoodPackageState(NeighborhoodPackageStateCreateDTO neighborhoodPackageState) {
        NeighborhoodPackageStateEntity neighborhoodPackageStateEntity = NeighborhoodPackageStateEntity.builder()
                .idNeighborhoodPackageState(UUID.randomUUID())
                .name(neighborhoodPackageState.getName())
                .build();

        return neighborhoodPackageStateRepository.insertNeighborhoodPackageState(neighborhoodPackageStateEntity.getIdNeighborhoodPackageState(), neighborhoodPackageStateEntity.getName())
                .thenReturn(neighborhoodPackageStateEntity)
                .onErrorMap(error -> {
                    System.err.println("Error al guardar estado de paquete de barrio: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving neighborhood package state", error);
                });
    }


    public Mono<Void> deleteNeighborhoodPackageStateById(UUID id) {
        return neighborhoodPackageStateRepository.findById(id)
                .flatMap(existingNeighborhoodPackageState -> neighborhoodPackageStateRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}