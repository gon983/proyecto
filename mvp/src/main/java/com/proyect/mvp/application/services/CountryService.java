import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyect.mvp.domain.model.entities.CountryEntity;
import com.proyect.mvp.domain.repository.CountryRepository;

import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.dao.DataIntegrityViolationException;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Flux<CountryEntity> getAllCountries() {
        return countryRepository.findAll()
                .onErrorResume(e -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener países")));
    }

    public Mono<CountryEntity> getCountryById(String id) {
        return countryRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "País no encontrado")));
    }

    public Mono<CountryEntity> createCountry(CountryEntity country) {
        return countryRepository.save(country)
                .onErrorResume(DataIntegrityViolationException.class, ex -> 
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un país con ese nombre")))
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear país")));
    }

    public Mono<Void> deleteCountry(String id) {
        return countryRepository.deleteById(id)
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar país")));
    }
}
