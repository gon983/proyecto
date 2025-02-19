package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.services.CollectionPointService;
import com.proyect.mvp.domain.model.entities.CollectionPointEntity;
import com.proyect.mvp.dtos.create.CollectionPointCreateDTO;
import com.proyect.mvp.dtos.update.CollectionPointUpdateDTO;
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
public class CollectionPointRouter {

    @Bean
    public RouterFunction<ServerResponse> collectionPointRoutes(CollectionPointService collectionPointService) {
        return route(GET("/collectionpoints"), request -> getAllCollectionPoints(collectionPointService))
                .andRoute(GET("/collectionpoints/{id}"), request -> getCollectionPointById(request, collectionPointService))
                .andRoute(POST("/collectionpoints"), request -> createCollectionPoint(request, collectionPointService))
                .andRoute(PUT("/collectionpoints/{id}"), request -> updateCollectionPoint(request, collectionPointService))
                .andRoute(DELETE("/collectionpoints/{id}"), request -> deleteCollectionPoint(request, collectionPointService));
    }

    private Mono<ServerResponse> getAllCollectionPoints(CollectionPointService collectionPointService) {
        return ServerResponse.ok().body(collectionPointService.getAllCollectionPoints(), CollectionPointEntity.class);
    }

    private Mono<ServerResponse> getCollectionPointById(ServerRequest request, CollectionPointService collectionPointService) {
        try {
            UUID id = UUID.fromString(request.pathVariable("id"));
            return collectionPointService.getCollectionPointById(id)
                    .flatMap(collectionPoint -> ServerResponse.ok().bodyValue(collectionPoint));
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
        }
    }

    private Mono<ServerResponse> createCollectionPoint(ServerRequest request, CollectionPointService collectionPointService) {
        return request.bodyToMono(CollectionPointCreateDTO.class)
                .flatMap(collectionPoint -> collectionPointService.saveNewCollectionPoint(collectionPoint))
                .flatMap(savedCollectionPoint -> ServerResponse.ok().bodyValue(savedCollectionPoint))
                .onErrorResume(ResponseStatusException.class, e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> updateCollectionPoint(ServerRequest request, CollectionPointService collectionPointService) {
        try {
            UUID id = UUID.fromString(request.pathVariable("id"));
            return request.bodyToMono(CollectionPointUpdateDTO.class)
                    .flatMap(collectionPoint -> collectionPointService.updateCollectionPoint(id, collectionPoint))
                    .flatMap(updatedCollectionPoint -> ServerResponse.ok().bodyValue(updatedCollectionPoint));
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
        }
    }

    private Mono<ServerResponse> deleteCollectionPoint(ServerRequest request, CollectionPointService collectionPointService) {
        try {
            UUID id = UUID.fromString(request.pathVariable("id"));
            return collectionPointService.deleteCollectionPointById(id)
                    .then(ServerResponse.noContent().build());
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
        }
    }
}