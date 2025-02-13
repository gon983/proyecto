package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.CountryEntity;
import com.proyect.mvp.domain.repository.CountryRepository;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Flux<CountryEntity> getAllCountries() {
        return countryRepository.findAll();
    }

    public Mono<CountryEntity> getCountryById(String id) {
        try {
            UUID uuid = UUID.fromString(id); // Convierte String a UUID
            return countryRepository.findById(uuid)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
        }
    }

   
    public Mono<Void> saveCountry(CountryEntity country) {
        CountryEntity countryEntity = new CountryEntity(country.getName());
        return countryRepository.insertCountry(countryEntity.getIdCountry(), countryEntity.getName())
                .onErrorMap(error -> {
                    System.err.println("Error al guardar: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving country", error);
                });
    }


    // public Mono<CountryEntity> updateCountry(String id, CountryEntity updatedCountry) {
    //     try {
    //         UUID uuid = UUID.fromString(id);
    //         return countryRepository.findById(uuid)
    //                 .flatMap(existingCountry -> {
    //                     existingCountry.setName(updatedCountry.getName());
    //                     // ... actualizar otros campos
    //                     return countryRepository.save(existingCountry);
    //                 })
    //                 .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    //     } catch (IllegalArgumentException e) {
    //         return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
    //     }
    // }

    public Mono<Void> deleteCountryById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return countryRepository.findById(uuid)
                    .flatMap(existingCountry -> countryRepository.deleteById(uuid))
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
        }
    }
}