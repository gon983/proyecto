package com.proyect.mvp.infrastructure.routes;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.proyect.mvp.application.services.StandarProductService;
import com.proyect.mvp.domain.model.entities.StandarProductEntity;
import com.proyect.mvp.dtos.create.StandarProductCreateDTO;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
@Configuration
public class StandarProductRouter {

    @Bean
    public RouterFunction<ServerResponse> standarProductRoutes(StandarProductService standarProductService) {
        return route(GET("/standarProducts"), request -> getAllStandarProducts(request, standarProductService))
                .andRoute(GET("/standarProducts/{id}"), request -> getStandarProductById(request, standarProductService))
                .andRoute(POST("/standarProducts"), request -> createStandarProduct(request, standarProductService));
        
    }
    
    private Mono<ServerResponse> getAllStandarProducts(ServerRequest request, StandarProductService standarProductService) {
        return ServerResponse.ok().body(standarProductService.getAllStandarProducts(), StandarProductEntity.class);
    }
    
    private Mono<ServerResponse> getStandarProductById(ServerRequest request, StandarProductService standarProductService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return ServerResponse.ok().body(standarProductService.getStandarProductById(id), StandarProductEntity.class);
    }
    
    private Mono<ServerResponse> createStandarProduct(ServerRequest request, StandarProductService standarProductService) {
        StandarProductCreateDTO standarProductDto = request.bodyToMono(StandarProductCreateDTO.class).block();
        return ServerResponse.ok().body(standarProductService.createStandarProduct(standarProductDto), StandarProductEntity.class);
    }
}
