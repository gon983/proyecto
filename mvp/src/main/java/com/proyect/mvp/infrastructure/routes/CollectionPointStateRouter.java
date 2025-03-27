package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.dtos.create.CollectionPointStateCreateDTO;
import com.proyect.mvp.application.services.CollectionPointStateService;
import com.proyect.mvp.domain.model.entities.CollectionPointStateEntity;

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
public class CollectionPointStateRouter {

    @Bean
    public RouterFunction<ServerResponse> collectionPointStateRoutes(CollectionPointStateService collectionPointStateService) {
        return route(POST("/api/admin/collectionPointStates"), request -> createCollectionPointState(request, collectionPointStateService))
                .andRoute(GET("/api/user/collectionPointStates"), request -> getAllCollectionPointStates(collectionPointStateService))
                .andRoute(GET("/api/user/collectionPointStates/{id}"), request -> getCollectionPointStateById(request, collectionPointStateService))
                .andRoute(DELETE("/api/admin/collectionPointStates/{id}"), request -> deleteCollectionPointStateById(request, collectionPointStateService)); // Ruta para eliminar
    }

    private Mono<ServerResponse> createCollectionPointState(ServerRequest request, CollectionPointStateService collectionPointStateService) {
        return request.bodyToMono(CollectionPointStateCreateDTO.class)
                .flatMap(collectionPointState -> collectionPointStateService.saveNewCollectionPointState(collectionPointState))
                .flatMap(savedCollectionPointState -> ServerResponse.ok().bodyValue(savedCollectionPointState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getAllCollectionPointStates(CollectionPointStateService collectionPointStateService) {
        return ServerResponse.ok().body(collectionPointStateService.getAllCollectionPointStates(), CollectionPointStateEntity.class);
    }

    private Mono<ServerResponse> getCollectionPointStateById(ServerRequest request, CollectionPointStateService collectionPointStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return collectionPointStateService.getCollectionPointStateById(id)
                .flatMap(collectionPointState -> ServerResponse.ok().bodyValue(collectionPointState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> deleteCollectionPointStateById(ServerRequest request, CollectionPointStateService collectionPointStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return collectionPointStateService.deleteCollectionPointStateById(id)
                .flatMap(v -> ServerResponse.noContent().build()) // Respuesta 204 No Content
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
}

