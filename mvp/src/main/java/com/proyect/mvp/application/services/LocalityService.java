package com.proyect.mvp.application.services;

import com.proyect.mvp.application.dtos.create.LocalityCreateDTO;
import com.proyect.mvp.domain.model.entities.LocalityEntity;
import com.proyect.mvp.domain.repository.LocalityRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class LocalityService {

    private final LocalityRepository localityRepository;

    public LocalityService(LocalityRepository localityRepository) {
        this.localityRepository = localityRepository;
    }

    public Flux<LocalityEntity> getAllLocalities() {
        return localityRepository.findAll();
    }

    public Mono<LocalityEntity> getLocalityById(UUID id) {
        return localityRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<LocalityEntity> saveNewLocality(LocalityCreateDTO locality) {
        LocalityEntity localityEntity = LocalityEntity.builder()
                .idLocality(UUID.randomUUID())
                .name(locality.getName())
                .cityId(locality.getCityId())
                .build();
        return localityRepository.insertLocality(localityEntity.getIdLocality(), localityEntity.getName(), localityEntity.getCityId())
                .thenReturn(localityEntity)
                .onErrorMap(error -> {
                    System.err.println("Error al guardar localidad: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving locality", error);
                });
    }

    public Mono<LocalityEntity> updateLocality(UUID id, LocalityEntity updatedLocality) {
        return localityRepository.findById(id)
                .flatMap(existingLocality -> {
                    LocalityEntity newLocality = LocalityEntity.builder()
                            .idLocality(id)
                            .name(updatedLocality.getName())
                            .cityId(updatedLocality.getCityId())
                            .build();
                    return localityRepository.save(newLocality);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<Void> deleteLocalityById(UUID id) {
        return localityRepository.findById(id)
                .flatMap(existingLocality -> localityRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
