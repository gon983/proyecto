package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.CityEntity;
import com.proyect.mvp.domain.repository.CityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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

    public Mono<CityEntity> saveCity(CityEntity city) {
        CityEntity cityEntity = new CityEntity(city.getName(), city.getCountryId()); // Asegúrate de que CityEntity tenga el countryId
        return cityRepository.insertCity(cityEntity.getIdCity(), cityEntity.getName(), cityEntity.getCountryId())
                .onErrorMap(error -> {
                    System.err.println("Error al guardar ciudad: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving city", error);
                });
    }

    public Mono<CityEntity> updateCity(UUID id, CityEntity updatedCity) {
        return cityRepository.findById(id)
                .flatMap(existingCity -> {
                    existingCity.setName(updatedCity.getName());
                    existingCity.setCountryId(updatedCity.getCountryId()); // Actualiza el countryId si es necesario
                    // ... actualizar otros campos si es necesario
                    return cityRepository.save(existingCity);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<Void> deleteCityById(UUID id) {
        return cityRepository.findById(id)
                .flatMap(existingCity -> cityRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Flux<CityEntity> getCitiesByCountryId(UUID countryId) {
        return cityRepository.findByCountryId(countryId);
    }
}