package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.dtos.create.PurchaseStateCreateDTO;
import com.proyect.mvp.application.services.PurchaseStateService;
import com.proyect.mvp.domain.model.entities.PurchaseStateEntity;

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
public class PurchaseStateRouter {

    @Bean
    public RouterFunction<ServerResponse> purchaseStateRoutes(PurchaseStateService purchaseStateService) {
        return route(POST("/api/admin/purchaseStates"), request -> createPurchaseState(request, purchaseStateService))
                .andRoute(GET("/api/user/purchaseStates"), request -> getAllPurchaseStates(purchaseStateService))
                .andRoute(GET("/api/user/purchaseStates/{id}"), request -> getPurchaseStateById(request, purchaseStateService))
                .andRoute(DELETE("/api/admin/purchaseStates/{id}"), request -> deletePurchaseStateById(request, purchaseStateService));
    }

    private Mono<ServerResponse> createPurchaseState(ServerRequest request, PurchaseStateService purchaseStateService) {
        return request.bodyToMono(PurchaseStateCreateDTO.class)
                .flatMap(purchaseState -> purchaseStateService.saveNewPurchaseState(purchaseState))
                .flatMap(savedPurchaseState -> ServerResponse.ok().bodyValue(savedPurchaseState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getAllPurchaseStates(PurchaseStateService purchaseStateService) {
        return ServerResponse.ok().body(purchaseStateService.getAllPurchaseStates(), PurchaseStateEntity.class);
    }

    private Mono<ServerResponse> getPurchaseStateById(ServerRequest request, PurchaseStateService purchaseStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return purchaseStateService.getPurchaseStateById(id)
                .flatMap(purchaseState -> ServerResponse.ok().bodyValue(purchaseState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> deletePurchaseStateById(ServerRequest request, PurchaseStateService purchaseStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return purchaseStateService.deletePurchaseStateById(id)
                .flatMap(v -> ServerResponse.noContent().build())
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
}
