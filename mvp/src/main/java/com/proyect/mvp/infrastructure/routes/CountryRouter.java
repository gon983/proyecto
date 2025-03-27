package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.dtos.create.CountryCreateDTO;
import com.proyect.mvp.application.dtos.update.CountryUpdateDTO;
import com.proyect.mvp.application.services.CountryService;
import com.proyect.mvp.domain.model.entities.CountryEntity;

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
public class CountryRouter {

    @Bean
    public RouterFunction<ServerResponse> countryRoutes(CountryService countryService) {
        return route(GET("/public/countries"), request -> getAllCountries(countryService))
                .andRoute(GET("/countries/{id}"), request -> getCountryById(request, countryService))
                .andRoute(POST("/countries"), request -> createCountry(request, countryService))
                .andRoute(PUT("/countries/{id}"), request -> updateCountry(request, countryService))
                .andRoute(DELETE("/countries/{id}"), request -> deleteCountry(request, countryService));
    }

    private Mono<ServerResponse> getAllCountries(CountryService countryService) {
        return ServerResponse.ok().body(countryService.getAllCountries(), CountryEntity.class);
    }

    private Mono<ServerResponse> getCountryById(ServerRequest request, CountryService countryService) {
        try {
            UUID id = UUID.fromString(request.pathVariable("id"));
            return countryService.getCountryById(id)
                    .flatMap(country -> ServerResponse.ok().bodyValue(country));
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
        }
    }

    private Mono<ServerResponse> createCountry(ServerRequest request, CountryService countryService) {
        return request.bodyToMono(CountryCreateDTO.class)
                .flatMap(country -> countryService.saveNewCountry(country))
                .flatMap(savedCountry -> ServerResponse.ok().bodyValue(savedCountry))
                .onErrorResume(ResponseStatusException.class, e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> updateCountry(ServerRequest request, CountryService countryService) {
        try {
            UUID id = UUID.fromString(request.pathVariable("id"));
            return request.bodyToMono(CountryUpdateDTO.class)
                    .flatMap(country -> countryService.updateCountry(id, country))
                    .flatMap(updatedCountry -> ServerResponse.ok().bodyValue(updatedCountry));
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
        }
    }

    private Mono<ServerResponse> deleteCountry(ServerRequest request, CountryService countryService) {
        try {
            UUID id = UUID.fromString(request.pathVariable("id"));
            return countryService.deleteCountryById(id)
                    .then(ServerResponse.noContent().build());
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
        }
    }
}