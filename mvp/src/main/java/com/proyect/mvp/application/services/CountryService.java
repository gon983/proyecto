package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.CountryEntity;
import com.proyect.mvp.domain.repository.CountryRepository;


import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;


@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    // Crear o actualizar un país
    public Mono<CountryEntity> saveCountry(CountryEntity country) {
        return countryRepository.save(country)
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving country", error));
    }

    // Obtener un país por ID
    public Mono<CountryEntity> getCountryById(String id) {
        return countryRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found with id: " + id)));
    }

    // Obtener todos los países
    public Flux<CountryEntity> getAllCountries() {
        return countryRepository.findAll()
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving countries", error));
    }

    // Eliminar un país por ID
    public Mono<Void> deleteCountryById(String id) {
        return countryRepository.findById(id)
                .flatMap(existingCountry -> countryRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found with id: " + id)));
    }

    // Actualizar un país
    public Mono<CountryEntity> updateCountry(String id, CountryEntity updatedCountry) {
        return countryRepository.findById(id)
                .flatMap(existingCountry -> {
                    existingCountry.setName(updatedCountry.getName());
                    return countryRepository.save(existingCountry);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found with id: " + id)))
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating country", error));
    }
}