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

    public Mono<CountryEntity> getCountryById(UUID id) {
        return countryRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<CountryEntity> saveCountry(CountryEntity country) {
        CountryEntity countryEntity = new CountryEntity(country.getName());
        return countryRepository.insertCountry(countryEntity.getIdCountry(), countryEntity.getName())
            .onErrorMap(error -> {
                System.err.println("Error al guardar: " + error.getMessage());
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving country", error);
            });
    }

    public Mono<CountryEntity> updateCountry(UUID id, CountryEntity updatedCountry) {
        return countryRepository.findById(id)
            .flatMap(existingCountry -> {
                existingCountry.setName(updatedCountry.getName());
                // ... actualizar otros campos si es necesario
                return countryRepository.save(existingCountry);
            })
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<Void> deleteCountryById(UUID id) {
        return countryRepository.findById(id)
            .flatMap(existingCountry -> countryRepository.deleteById(id))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}