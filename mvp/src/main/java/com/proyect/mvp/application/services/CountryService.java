package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.CountryEntity;
import com.proyect.mvp.domain.repository.CityRepository;
import com.proyect.mvp.domain.repository.CountryRepository;
import com.proyect.mvp.dtos.create.CountryCreateDTO;
import com.proyect.mvp.dtos.update.CountryUpdateDTO;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.UUID;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public CountryService(CountryRepository countryRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        
    }

    public Flux<CountryEntity> getAllCountries() {
        return countryRepository.findAll()
            .flatMap(country -> cityRepository.findByCountryId(country.getIdCountry())
                .collectList()
                .doOnNext(cities -> {
                    // Agregar log para verificar si están llegando ciudades
                    System.out.println("Ciudades encontradas para país " + country.getIdCountry() + ": " + cities.size());
                })
                .map(cities -> {
                    country.insertCities(new HashSet<>(cities));
                    return country;
                })
            );
    }

    public Mono<CountryEntity> getCountryById(UUID id) {
    return countryRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))) // Manejo de error si el país no existe
            .flatMap(country -> cityRepository.findByCountryId(country.getIdCountry()) // Buscar ciudades del país
                    .collectList()
                    .map(cities -> {
                        country.insertCities(new HashSet<>(cities)); // Agregar ciudades al país
                        return country;
                    })
            );
}


    public Mono<CountryEntity> saveNewCountry(CountryCreateDTO country) {
        CountryEntity countryEntity = CountryEntity.builder()
            .idCountry(UUID.randomUUID())
            .name(country.getName())
            .build();
    
        return countryRepository.insertCountry(countryEntity.getIdCountry(), countryEntity.getName())
            .thenReturn(countryEntity) // Devuelve el Mono<CountryEntity>
            .onErrorMap(error -> {
                System.err.println("Error al guardar: " + error.getMessage());
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving country", error);
            });
    }

    public Mono<CountryEntity> updateCountry(UUID id, CountryUpdateDTO updatedCountry) {
        return countryRepository.findById(id)
                .flatMap(existingCountry -> {
                    CountryEntity newCountry = CountryEntity.builder()
                            .idCountry(id)
                            .name(updatedCountry.getName())
                            .build();
                    return countryRepository.save(newCountry);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<Void> deleteCountryById(UUID id) {
        return countryRepository.findById(id)
                .flatMap(existingCountry -> countryRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}