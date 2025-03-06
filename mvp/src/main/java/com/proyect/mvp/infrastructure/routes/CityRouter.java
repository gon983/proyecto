package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.services.CityService;
import com.proyect.mvp.domain.model.dtos.create.CityCreateDTO;
import com.proyect.mvp.domain.model.entities.CityEntity;

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
public class CityRouter {

    @Bean
    public RouterFunction<ServerResponse> cityRoutes(CityService cityService) {
        return route(POST("/cities"), request -> createCity(request, cityService))
                .andRoute(GET("/cities"), request -> getAllCities(cityService))
                .andRoute(GET("/cities/{id}"), request -> getCityById(request, cityService));
                
    }

    private Mono<ServerResponse> createCity(ServerRequest request, CityService cityService) {
        return request.bodyToMono(CityCreateDTO.class)
                .flatMap(city -> cityService.saveNewCity(city))
                .flatMap(savedCity -> ServerResponse.ok().bodyValue(savedCity))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getAllCities(CityService cityService) {
        return ServerResponse.ok().body(cityService.getAllCities(), CityEntity.class);
    }

    private Mono<ServerResponse> getCityById(ServerRequest request, CityService cityService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return cityService.getCityById(id)
                .flatMap(city -> ServerResponse.ok().bodyValue(city))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    
}