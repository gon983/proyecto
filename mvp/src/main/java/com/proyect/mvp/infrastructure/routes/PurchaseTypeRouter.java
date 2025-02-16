package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.services.PurchaseTypeService;
import com.proyect.mvp.domain.model.entities.PurchaseTypeEntity;
import com.proyect.mvp.dtos.create.PurchaseTypeCreateDTO;
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
public class PurchaseTypeRouter {

    @Bean
    public RouterFunction<ServerResponse> purchaseTypeRoutes(PurchaseTypeService purchaseTypeService) {
        return route(POST("/purchaseTypes"), request -> createPurchaseType(request, purchaseTypeService))
                .andRoute(GET("/purchaseTypes"), request -> getAllPurchaseTypes(purchaseTypeService))
                .andRoute(GET("/purchaseTypes/{id}"), request -> getPurchaseTypeById(request, purchaseTypeService))
                .andRoute(DELETE("/purchaseTypes/{id}"), request -> deletePurchaseTypeById(request, purchaseTypeService));
    }

    private Mono<ServerResponse> createPurchaseType(ServerRequest request, PurchaseTypeService purchaseTypeService) {
        return request.bodyToMono(PurchaseTypeCreateDTO.class)
                .flatMap(purchaseType -> purchaseTypeService.saveNewPurchaseType(purchaseType))
                .flatMap(savedPurchaseType -> ServerResponse.ok().bodyValue(savedPurchaseType))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getAllPurchaseTypes(PurchaseTypeService purchaseTypeService) {
        return ServerResponse.ok().body(purchaseTypeService.getAllPurchaseTypes(), PurchaseTypeEntity.class);
    }

    private Mono<ServerResponse> getPurchaseTypeById(ServerRequest request, PurchaseTypeService purchaseTypeService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return purchaseTypeService.getPurchaseTypeById(id)
                .flatMap(purchaseType -> ServerResponse.ok().bodyValue(purchaseType))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> deletePurchaseTypeById(ServerRequest request, PurchaseTypeService purchaseTypeService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return purchaseTypeService.deletePurchaseTypeById(id)
                .flatMap(v -> ServerResponse.noContent().build())
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
}