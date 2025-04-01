package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.dtos.create.CityCreateDTO;
import com.proyect.mvp.application.services.CityService;
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
        return route(POST("/api/admin/cities"), request -> createCity(request, cityService))
                .andRoute(GET("/api/user/{idCountry}/cities"), request -> getCitiesFromCountry(request, cityService))
                .andRoute(GET("/api/user/cities/{id}"), request -> getCityById(request, cityService));
                
    }

    private Mono<ServerResponse> createCity(ServerRequest request, CityService cityService) {
        return request.bodyToMono(CityCreateDTO.class)
                .flatMap(city -> cityService.saveNewCity(city))
                .flatMap(savedCity -> ServerResponse.ok().bodyValue(savedCity))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getCitiesFromCountry(ServerRequest request, CityService cityService) {
        UUID idCountry = UUID.fromString(request.pathVariable("idCountry"));
        return ServerResponse.ok().bodyValue(cityService.getAllCitiesFromCountry(idCountry));
    }

    private Mono<ServerResponse> getCityById(ServerRequest request, CityService cityService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return cityService.getCityById(id)
                .flatMap(city -> ServerResponse.ok().bodyValue(city))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    
}