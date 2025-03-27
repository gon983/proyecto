package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.dtos.create.LocalityCreateDTO;
import com.proyect.mvp.application.services.LocalityService;
import com.proyect.mvp.domain.model.entities.LocalityEntity;

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
public class LocalityRouter {

    @Bean
    public RouterFunction<ServerResponse> localityRoutes(LocalityService localityService) {
        return route(POST("/api/admin/localities"), request -> createLocality(request, localityService))
                .andRoute(GET("/api/user/localities"), request -> getAllLocalities(localityService))
                .andRoute(GET("/api/user/localities/{id}"), request -> getLocalityById(request, localityService));
    }

    private Mono<ServerResponse> createLocality(ServerRequest request, LocalityService localityService) {
        return request.bodyToMono(LocalityCreateDTO.class)
                .flatMap(locality -> localityService.saveNewLocality(locality))
                .flatMap(savedLocality -> ServerResponse.ok().bodyValue(savedLocality))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getAllLocalities(LocalityService localityService) {
        return ServerResponse.ok().body(localityService.getAllLocalities(), LocalityEntity.class);
    }

    private Mono<ServerResponse> getLocalityById(ServerRequest request, LocalityService localityService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return localityService.getLocalityById(id)
                .flatMap(locality -> ServerResponse.ok().bodyValue(locality))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
}
