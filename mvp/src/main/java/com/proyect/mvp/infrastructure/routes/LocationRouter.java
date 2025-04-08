package com.proyect.mvp.infrastructure.routes;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.proyect.mvp.application.dtos.create.LocationCreateDTO;
import com.proyect.mvp.application.services.LocationService;
import com.proyect.mvp.infrastructure.security.UserContextService;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.UUID;



@Configuration
public class LocationRouter {

    @Bean
    public RouterFunction<ServerResponse> locationRoutes(LocationService locationService, UserContextService userContext) {
        return route(GET("/api/user/{userId}/locations"), request -> getLocationsByUser(request, locationService))
                .andRoute(DELETE("/api/user/{userId}/locations/{locationId}"), request -> deleteLocation(request, locationService))
                .andRoute(POST("/api/locations"), request -> createLocation(request, locationService));
    }

    private Mono<ServerResponse> getLocationsByUser(ServerRequest request, LocationService locationService) {
        UUID userId = UUID.fromString(request.pathVariable("userId"));
        return locationService.getLocationsByUser(userId)
                .collectList()
                .flatMap(locations -> ServerResponse.ok().bodyValue(locations));
    }

    private Mono<ServerResponse> deleteLocation(ServerRequest request, LocationService locationService) {
        UUID locationId = UUID.fromString(request.pathVariable("locationId"));
        return locationService.deleteLocation(locationId)
                .then(ServerResponse.noContent().build());
    }

    private Mono<ServerResponse> createLocation(ServerRequest request, LocationService locationService) {
        return request.bodyToMono(LocationCreateDTO.class)
                .flatMap(locationService::createLocation)
                .flatMap(savedLocation -> ServerResponse.ok().bodyValue(savedLocation));
    }
}
