package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.domain.model.entities.CountryEntity;
import com.proyect.mvp.application.services.CountryService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration

public class CountryRoute {

    @Bean
    public RouterFunction<ServerResponse> routes(CountryService countryService) {
        return route(GET("/countries"), request ->
                    ServerResponse.ok().body(countryService.getAllCountries(), CountryEntity.class))
                .andRoute(GET("/countries/{id}"), request -> {
                    String id = request.pathVariable("id");
                    return countryService.getCountryById(id)
                            .flatMap(country -> ServerResponse.ok().bodyValue(country))
                            .onErrorResume(ResponseStatusException.class, e ->
                                    ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
                })
                .andRoute(POST("/countries"), request ->
                        request.bodyToMono(CountryEntity.class)
                                .flatMap(countryService::saveCountry)
                                .flatMap(savedCountry -> ServerResponse.ok().bodyValue(savedCountry))
                                .onErrorResume(ResponseStatusException.class, e ->
                                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage())))
                .andRoute(PUT("/countries/{id}"), request ->
                        request.bodyToMono(CountryEntity.class)
                                .flatMap(updatedCountry -> {
                                    String id = request.pathVariable("id");
                                    return countryService.updateCountry(id, updatedCountry);
                                })
                                .flatMap(updatedCountry -> ServerResponse.ok().bodyValue(updatedCountry))
                                .onErrorResume(ResponseStatusException.class, e ->
                                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage())))
                .andRoute(DELETE("/countries/{id}"), request -> {
                    String id = request.pathVariable("id");
                    return countryService.deleteCountryById(id)
                            .then(ServerResponse.noContent().build())
                            .onErrorResume(ResponseStatusException.class, e ->
                                    ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
                });
    }
}