package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.dtos.create.NeighborhoodCreateDTO;
import com.proyect.mvp.application.services.NeighborhoodService;
import com.proyect.mvp.domain.model.entities.NeighborhoodEntity;

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
public class NeighborhoodRouter {

    @Bean
    public RouterFunction<ServerResponse> neighborhoodRoutes(NeighborhoodService neighborhoodService) {
        return route(POST("/api/user/neighborhoods"), request -> createNeighborhood(request, neighborhoodService))
                .andRoute(GET("/api/admin/neighborhoods"), request -> getAllNeighborhoods(neighborhoodService))
                .andRoute(GET("/api/admin/neighborhoods/{id}"), request -> getNeighborhoodById(request, neighborhoodService));
    }

    private Mono<ServerResponse> createNeighborhood(ServerRequest request, NeighborhoodService neighborhoodService) {
        return request.bodyToMono(NeighborhoodCreateDTO.class)
                .flatMap(neighborhood -> neighborhoodService.saveNewNeighborhood(neighborhood))
                .flatMap(savedNeighborhood -> ServerResponse.ok(200).bodyValue(savedNeighborhood))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getAllNeighborhoods(NeighborhoodService neighborhoodService) {
        return ServerResponse.ok(200).body(neighborhoodService.getAllNeighborhoods(), NeighborhoodEntity.class);
    }

    private Mono<ServerResponse> getNeighborhoodById(ServerRequest request, NeighborhoodService neighborhoodService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return neighborhoodService.getNeighborhoodById(id)
                .flatMap(neighborhood -> ServerResponse.ok(200).bodyValue(neighborhood))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
}
