package com.proyect.mvp.application.services;

import com.proyect.mvp.application.dtos.create.CityCreateDTO;
import com.proyect.mvp.domain.model.entities.CityEntity;
import com.proyect.mvp.domain.repository.CityRepository;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Flux<CityEntity> getAllCities() {
        return cityRepository.findAll();
    }

    public Mono<CityEntity> getCityById(UUID id) {
        return cityRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<CityEntity> saveNewCity(CityCreateDTO city) {
        CityEntity cityEntity = CityEntity.builder()
                .idCity(UUID.randomUUID())
                .name(city.getName())
                .countryId(city.getCountryId())
                .build();
        return cityRepository.insertCity(cityEntity.getIdCity(), cityEntity.getName(), cityEntity.getCountryId())
                .thenReturn(cityEntity)
                .onErrorMap(error -> {
                    System.err.println("Error al guardar ciudad: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving city", error);
                });
    }

    public Mono<CityEntity> updateCity(UUID id, CityEntity updatedCity) {
        return cityRepository.findById(id)
                .flatMap(existingCity -> {
                    CityEntity newCity = CityEntity.builder()
                            .idCity(id)
                            .name(updatedCity.getName())
                            .countryId(updatedCity.getCountryId())
                            .build();
                    return cityRepository.save(newCity);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<Void> deleteCityById(UUID id) {
        return cityRepository.findById(id)
                .flatMap(existingCity -> cityRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    
}