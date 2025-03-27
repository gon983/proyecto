package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.dtos.create.PurchaseDetailStateCreateDTO;
import com.proyect.mvp.application.services.PurchaseDetailStateService;
import com.proyect.mvp.domain.model.entities.PurchaseDetailStateEntity;

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
public class PurchaseDetailStateRouter {

    @Bean
    public RouterFunction<ServerResponse> purchaseDetailStateRoutes(PurchaseDetailStateService purchaseDetailStateService) {
        return route(POST("/api/admin/purchaseDetailStates"), request -> createPurchaseDetailState(request, purchaseDetailStateService))
                .andRoute(GET("/api/user/purchaseDetailStates"), request -> getAllPurchaseDetailStates(purchaseDetailStateService))
                .andRoute(GET("/api/user/purchaseDetailStates/{id}"), request -> getPurchaseDetailStateById(request, purchaseDetailStateService))
                .andRoute(DELETE("/api/admin/purchaseDetailStates/{id}"), request -> deletePurchaseDetailStateById(request, purchaseDetailStateService));
    }

    private Mono<ServerResponse> createPurchaseDetailState(ServerRequest request, PurchaseDetailStateService purchaseDetailStateService) {
        return request.bodyToMono(PurchaseDetailStateCreateDTO.class)
                .flatMap(purchaseDetailState -> purchaseDetailStateService.saveNewPurchaseDetailState(purchaseDetailState))
                .flatMap(savedPurchaseDetailState -> ServerResponse.ok().bodyValue(savedPurchaseDetailState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getAllPurchaseDetailStates(PurchaseDetailStateService purchaseDetailStateService) {
        return ServerResponse.ok().body(purchaseDetailStateService.getAllPurchaseDetailStates(), PurchaseDetailStateEntity.class);
    }

    private Mono<ServerResponse> getPurchaseDetailStateById(ServerRequest request, PurchaseDetailStateService purchaseDetailStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return purchaseDetailStateService.getPurchaseDetailStateById(id)
                .flatMap(purchaseDetailState -> ServerResponse.ok().bodyValue(purchaseDetailState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> deletePurchaseDetailStateById(ServerRequest request, PurchaseDetailStateService purchaseDetailStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return purchaseDetailStateService.deletePurchaseDetailStateById(id)
                .flatMap(v -> ServerResponse.noContent().build())
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
}
