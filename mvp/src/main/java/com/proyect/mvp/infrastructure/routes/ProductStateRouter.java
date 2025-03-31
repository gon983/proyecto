package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.dtos.create.ProductStateCreateDTO;
import com.proyect.mvp.application.services.ProductStateService;
import com.proyect.mvp.domain.model.entities.ProductStateEntity;

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
public class ProductStateRouter {

    @Bean
    public RouterFunction<ServerResponse> productStateRoutes(ProductStateService productStateService) {
        return route(POST("/api/admin/productStates"), request -> createProductState(request, productStateService))
                .andRoute(GET("/api/user/productStates"), request -> getAllProductStates(productStateService))
                .andRoute(GET("/api/user/productStates/{id}"), request -> getProductStateById(request, productStateService))
                .andRoute(DELETE("/api/admin/productStates/{id}"), request -> deleteProductStateById(request, productStateService));
    }

    private Mono<ServerResponse> createProductState(ServerRequest request, ProductStateService productStateService) {
        return request.bodyToMono(ProductStateCreateDTO.class)
                .flatMap(productState -> productStateService.saveNewProductState(productState))
                .flatMap(savedProductState -> ServerResponse.ok(200).bodyValue(savedProductState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getAllProductStates(ProductStateService productStateService) {
        return ServerResponse.ok(200).body(productStateService.getAllProductStates(), ProductStateEntity.class);
    }

    private Mono<ServerResponse> getProductStateById(ServerRequest request, ProductStateService productStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return productStateService.getProductStateById(id)
                .flatMap(productState -> ServerResponse.ok(200).bodyValue(productState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> deleteProductStateById(ServerRequest request, ProductStateService productStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return productStateService.deleteProductStateById(id)
                .flatMap(v -> ServerResponse.noContent().build())
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
}
