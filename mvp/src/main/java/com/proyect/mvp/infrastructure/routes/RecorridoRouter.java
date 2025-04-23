package com.proyect.mvp.infrastructure.routes;


import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.proyect.mvp.application.dtos.create.RecorridoCreateDTO;
import com.proyect.mvp.application.dtos.update.RecorridoUpdateDTO;
import com.proyect.mvp.application.services.RecorridoService;
import com.proyect.mvp.domain.model.entities.RecorridoEntity;

import reactor.core.publisher.Mono;

@Configuration
public class RecorridoRouter {
    
    @Bean
    public RouterFunction<ServerResponse> recorridoRoutes(RecorridoService recorridoService) {
        return route(GET("/api/admin/recorridos"), request -> getAllRecorridosActivos(request, recorridoService))
                .andRoute(GET("/api/admin/recorridos/{id}"), request -> getRecorridoById(request, recorridoService))
                .andRoute(POST("/api/admin/crearRecorrido"), request -> createRecorrido(request, recorridoService))
                .andRoute(PUT("/api/admin/modificarRecorrido"), request -> putRecorrido(request, recorridoService))
                .andRoute(DELETE("/api/admin/finalizarRecorrido/{id}"), request -> finalizarRecorrido(request, recorridoService));
    }
    
    private Mono<ServerResponse> createRecorrido(ServerRequest request, RecorridoService recorridoService) {
        return request.bodyToMono(RecorridoCreateDTO.class)
                .flatMap(recorrido -> recorridoService.saveNewRecorrido(recorrido))
                .flatMap(savedRecorrido -> ServerResponse.ok().bodyValue(savedRecorrido));
    }
    
    private Mono<ServerResponse> putRecorrido(ServerRequest request, RecorridoService recorridoService) {
        return request.bodyToMono(RecorridoUpdateDTO.class)
                .flatMap(recorrido -> recorridoService.putRecorrido(recorrido))
                .flatMap(savedRecorrido -> ServerResponse.ok().bodyValue(savedRecorrido));
    }
    
    private Mono<ServerResponse> getRecorridoById(ServerRequest request, RecorridoService recorridoService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return recorridoService.getRecorridoById(id)
                .flatMap(recorrido -> ServerResponse.ok().bodyValue(recorrido));
    }
    
    private Mono<ServerResponse> getAllRecorridosActivos(ServerRequest request, RecorridoService recorridoService) {
        return ServerResponse.ok().body(recorridoService.getAllRecorridosActivos(), RecorridoEntity.class);
    }
    
    private Mono<ServerResponse> finalizarRecorrido(ServerRequest request, RecorridoService recorridoService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return recorridoService.finalizarRecorrido(id)
                .then(ServerResponse.ok().build());
    }
}