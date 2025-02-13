package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.domain.model.dtos.CountryDTO;
import com.proyect.mvp.domain.model.entities.CountryEntity;
import com.proyect.mvp.application.services.CountryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CountryRoute {

    @Bean
    public RouterFunction<ServerResponse> routes(CountryService countryService) {
        return route(GET("/countries"), request -> getAllCountries(countryService))
                .andRoute(GET("/countries/{id}"), request -> getCountryById(request, countryService))
                .andRoute(POST("/countries"), request -> createCountry(request, countryService))
                .andRoute(PUT("/countries/{id}"), request -> updateCountry(request, countryService))
                .andRoute(DELETE("/countries/{id}"), request -> deleteCountry(request, countryService));
    }

    private Mono<ServerResponse> getAllCountries(CountryService countryService) {
        return ServerResponse.ok().body(countryService.getAllCountries(), CountryEntity.class);
    }

    private Mono<ServerResponse> getCountryById(ServerRequest request, CountryService countryService) {
        UUID id = UUID.fromString(request.pathVariable("id")); // Obtiene el UUID directamente del path variable
        return countryService.getCountryById(id)
                .flatMap(country -> ServerResponse.ok().bodyValue(country))
                .onErrorResume(IllegalArgumentException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format")))
                .onErrorResume(ResponseStatusException.class, e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> createCountry(ServerRequest request, CountryService countryService) {
        return request.bodyToMono(CountryDTO.class)
                .map(dto -> new CountryEntity(dto.getName())) // Crea la entidad usando el constructor con name
                .flatMap(countryService::saveCountry)
                .flatMap(savedCountry -> ServerResponse.ok().bodyValue(savedCountry))
                .onErrorResume(ResponseStatusException.class, e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> updateCountry(ServerRequest request, CountryService countryService) {
        UUID id = UUID.fromString(request.pathVariable("id")); // Obtiene el UUID directamente del path variable
        return request.bodyToMono(CountryDTO.class) // Recibe el DTO con el UUID
                .map(dto -> new CountryEntity(dto.getName())) // Mapea a la entidad
                .flatMap(country -> countryService.updateCountry(id, country)) // Llama al servicio con UUID y entidad
                .flatMap(updatedCountry -> ServerResponse.ok().bodyValue(updatedCountry))
                .onErrorResume(IllegalArgumentException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format")))
                .onErrorResume(ResponseStatusException.class, e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> deleteCountry(ServerRequest request, CountryService countryService) {
        UUID id = UUID.fromString(request.pathVariable("id")); // Obtiene el UUID directamente del path variable
        return countryService.deleteCountryById(id)
                .then(ServerResponse.noContent().build())
                .onErrorResume(IllegalArgumentException.class, e -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format")))
                .onErrorResume(ResponseStatusException.class, e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
}