package com.proyect.mvp.application.services;

import com.proyect.mvp.application.dtos.create.NeighborhoodCreateDTO;
import com.proyect.mvp.domain.model.entities.NeighborhoodEntity;
import com.proyect.mvp.domain.repository.NeighborhoodRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class NeighborhoodService {

    private final NeighborhoodRepository neighborhoodRepository;

    public NeighborhoodService(NeighborhoodRepository neighborhoodRepository) {
        this.neighborhoodRepository = neighborhoodRepository;
    }

    public Flux<NeighborhoodEntity> getAllNeighborhoods() {
        return neighborhoodRepository.findAll();
    }

    public Mono<NeighborhoodEntity> getNeighborhoodById(UUID id) {
        return neighborhoodRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<NeighborhoodEntity> saveNewNeighborhood(NeighborhoodCreateDTO neighborhood) {
        NeighborhoodEntity neighborhoodEntity = NeighborhoodEntity.builder()
                .idNeighborhood(UUID.randomUUID())
                .name(neighborhood.getName())
                .localityId(neighborhood.getLocalityId())
                .build();
        return neighborhoodRepository.insertNeighborhood(neighborhoodEntity.getIdNeighborhood(), neighborhoodEntity.getName(), neighborhoodEntity.getLocalityId())
                .thenReturn(neighborhoodEntity)
                .onErrorMap(error -> {
                    System.err.println("Error al guardar barrio: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving neighborhood", error);
                });
    }

    public Mono<NeighborhoodEntity> updateNeighborhood(UUID id, NeighborhoodEntity updatedNeighborhood) {
        return neighborhoodRepository.findById(id)
                .flatMap(existingNeighborhood -> {
                    NeighborhoodEntity newNeighborhood = NeighborhoodEntity.builder()
                            .idNeighborhood(id)
                            .name(updatedNeighborhood.getName())
                            .localityId(updatedNeighborhood.getLocalityId())
                            .build();
                    return neighborhoodRepository.save(newNeighborhood);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<Void> deleteNeighborhoodById(UUID id) {
        return neighborhoodRepository.findById(id)
                .flatMap(existingNeighborhood -> neighborhoodRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}

