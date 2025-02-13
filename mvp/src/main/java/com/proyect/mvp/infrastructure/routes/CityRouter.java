package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.domain.model.dtos.CityDTO;
import com.proyect.mvp.domain.model.entities.CityEntity;
import com.proyect.mvp.application.services.CityService; // You'll likely need a CityService
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.UUID;

@Configuration
public class CityRouter {

    @Bean
    public RouterFunction<ServerResponse> cityRoutes(CityService cityService) {  // Inject CityService
        return route(POST("/cities"), request -> createCity(request, cityService)) // General city creation
                .andRoute(GET("/cities"), request -> getAllCities(cityService)) //Get all cities
                .andRoute(GET("/countries/{countryId}/cities"), request -> getCitiesByCountryId(request, cityService)); // Cities by country
    }

    private Mono<ServerResponse> createCity(ServerRequest request, CityService cityService) {
        return request.bodyToMono(CityDTO.class)
                .map(dto -> new CityEntity(dto.getName(), dto.getCountryId())) // Include countryId in CityEntity and DTO
                .flatMap(cityService::saveCity) // Use CityService
                .flatMap(savedCity -> ServerResponse.ok().bodyValue(savedCity))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
    private Mono<ServerResponse> getAllCities(CityService cityService) {
        return ServerResponse.ok().body(cityService.getAllCities(), CityEntity.class);
    }

    private Mono<ServerResponse> getCitiesByCountryId(ServerRequest request, CityService cityService) {
        UUID countryId = UUID.fromString(request.pathVariable("countryId"));
        return cityService.getCitiesByCountryId(countryId)
                .collectList() // Collect the Flux<CityEntity> into a Mono<List<CityEntity>>
                .flatMap(cities -> ServerResponse.ok().bodyValue(cities)) // Use bodyValue with the List
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
    


}