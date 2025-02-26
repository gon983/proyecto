package com.proyect.mvp.infrastructure.routes;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.proyect.mvp.application.services.PurchaseService;
import com.proyect.mvp.dtos.create.PurchaseCreateDTO;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PurchaseRouter {

    @Bean
    public RouterFunction<ServerResponse> purchaseRoutes(PurchaseService purchaseService) {
        return route(POST("/purchases"), request-> createPurchase(request, purchaseService))
                .andRoute(GET("/purchases/{idPurchase}"), request-> getPurchaseWithDetails(request, purchaseService));

    }

    private Mono<ServerResponse> createPurchase(ServerRequest request, PurchaseService purchaseService) {
        return request.bodyToMono(PurchaseCreateDTO.class)
                .flatMap(purchase -> purchaseService.createPurchase(purchase))
                .flatMap(savedPurchase -> ServerResponse.ok().bodyValue(savedPurchase));
    }

    private Mono<ServerResponse> getPurchaseWithDetails(ServerRequest request, PurchaseService purchaseService) {
        UUID idPurchase = UUID.fromString(request.pathVariable("idPurchase"));
        return purchaseService.getPurchaseWithDetails(idPurchase)
                .flatMap(purchase -> ServerResponse.ok().bodyValue(purchase))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
}
