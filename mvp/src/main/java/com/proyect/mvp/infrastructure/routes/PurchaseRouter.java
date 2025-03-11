package com.proyect.mvp.infrastructure.routes;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.proyect.mvp.application.dtos.create.PurchaseCreateDTO;
import com.proyect.mvp.application.services.PurchaseService;
import com.proyect.mvp.infrastructure.middlewares.ConfirmPurchaseMiddleware;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PurchaseRouter {

    @Bean
    public RouterFunction<ServerResponse> purchaseRoutes(PurchaseService purchaseService, ConfirmPurchaseMiddleware confirmPurchaseMiddleware) {
        return route(POST("/purchases"), request -> createPurchase(request, purchaseService))
                .andRoute(GET("/purchases/{idPurchase}"), request -> getPurchaseWithDetails(request, purchaseService))
                .andRoute(POST("/confirmPurchase/{idPurchase}"), request -> confirmPurchase(request, purchaseService))
                .andRoute(POST("/confirmPayment"), request ->  confirmPurchaseMiddleware.validate(request)
                                                                        .flatMap(valid -> valid ? confirmPayment(request, purchaseService) : ServerResponse.status(401).build()));
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

    private Mono<ServerResponse> confirmPurchase(ServerRequest request, PurchaseService purchaseService) {
        UUID idPurchase = UUID.fromString(request.pathVariable("idPurchase"));
        
        return purchaseService.confirmPurchase(idPurchase)
            .collectList() // Convertimos el Flux en un Mono<List<Preference>>
            .flatMap(preferences -> {
                if (preferences.isEmpty()) {
                    return ServerResponse.notFound().build();
                }
                return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(preferences);
            });
    }
    
    private Mono<ServerResponse> confirmPayment(ServerRequest request, PurchaseService purchaseService) {
        return ServerResponse.ok().build();
    }
    
}
