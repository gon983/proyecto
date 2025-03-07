package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.dtos.create.SaleStateCreateDTO;
import com.proyect.mvp.application.services.SaleStateService;
import com.proyect.mvp.domain.model.entities.SaleStateEntity;

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
public class SaleStateRouter {

    @Bean
    public RouterFunction<ServerResponse> saleStateRoutes(SaleStateService saleStateService) {
        return route(POST("/saleStates"), request -> createSaleState(request, saleStateService))
                .andRoute(GET("/saleStates"), request -> getAllSaleStates(saleStateService))
                .andRoute(GET("/saleStates/{id}"), request -> getSaleStateById(request, saleStateService))
                .andRoute(DELETE("/saleStates/{id}"), request -> deleteSaleStateById(request, saleStateService));
    }

    private Mono<ServerResponse> createSaleState(ServerRequest request, SaleStateService saleStateService) {
        return request.bodyToMono(SaleStateCreateDTO.class)
                .flatMap(saleState -> saleStateService.saveNewSaleState(saleState))
                .flatMap(savedSaleState -> ServerResponse.ok().bodyValue(savedSaleState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getAllSaleStates(SaleStateService saleStateService) {
        return ServerResponse.ok().body(saleStateService.getAllSaleStates(), SaleStateEntity.class);
    }

    private Mono<ServerResponse> getSaleStateById(ServerRequest request, SaleStateService saleStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return saleStateService.getSaleStateById(id)
                .flatMap(saleState -> ServerResponse.ok().bodyValue(saleState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> deleteSaleStateById(ServerRequest request, SaleStateService saleStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return saleStateService.deleteSaleStateById(id)
                .flatMap(v -> ServerResponse.noContent().build())
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
}
