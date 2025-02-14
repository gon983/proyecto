package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.services.NeighborhoodPackageStateService;
import com.proyect.mvp.domain.model.entities.NeighborhoodPackageStateEntity;
import com.proyect.mvp.dtos.create.NeighborhoodPackageStateCreateDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class NeighborhoodPackageStateRouter {

    @Bean
    public RouterFunction<ServerResponse> neighborhoodPackageStateRoutes(NeighborhoodPackageStateService neighborhoodPackageStateService) {
        return route(POST("/neighborhoodPackageStates"), request -> createNeighborhoodPackageState(request, neighborhoodPackageStateService))
                .andRoute(GET("/neighborhoodPackageStates"), request -> getAllNeighborhoodPackageStates(neighborhoodPackageStateService))
                .andRoute(GET("/neighborhoodPackageStates/{id}"), request -> getNeighborhoodPackageStateById(request, neighborhoodPackageStateService))
                .andRoute(DELETE("/neighborhoodPackageStates/{id}"), request -> deleteNeighborhoodPackageStateById(request, neighborhoodPackageStateService));
    }

    private Mono<ServerResponse> createNeighborhoodPackageState(ServerRequest request, NeighborhoodPackageStateService neighborhoodPackageStateService) {
        return request.bodyToMono(NeighborhoodPackageStateCreateDTO.class)
                .flatMap(neighborhoodPackageState -> neighborhoodPackageStateService.saveNewNeighborhoodPackageState(neighborhoodPackageState))
                .flatMap(savedNeighborhoodPackageState -> ServerResponse.ok().bodyValue(savedNeighborhoodPackageState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getAllNeighborhoodPackageStates(NeighborhoodPackageStateService neighborhoodPackageStateService) {
        return ServerResponse.ok().body(neighborhoodPackageStateService.getAllNeighborhoodPackageStates(), NeighborhoodPackageStateEntity.class);
    }

    private Mono<ServerResponse> getNeighborhoodPackageStateById(ServerRequest request, NeighborhoodPackageStateService neighborhoodPackageStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return neighborhoodPackageStateService.getNeighborhoodPackageStateById(id)
                .flatMap(neighborhoodPackageState -> ServerResponse.ok().bodyValue(neighborhoodPackageState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> deleteNeighborhoodPackageStateById(ServerRequest request, NeighborhoodPackageStateService neighborhoodPackageStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return neighborhoodPackageStateService.deleteNeighborhoodPackageStateById(id)
                .flatMap(v -> ServerResponse.noContent().build())
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
}