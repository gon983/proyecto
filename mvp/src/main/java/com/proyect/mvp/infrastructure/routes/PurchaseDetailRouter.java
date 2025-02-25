package com.proyect.mvp.infrastructure.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
 

import com.proyect.mvp.application.services.PurchaseDetailService;
import com.proyect.mvp.dtos.create.PurchaseDetailCreateDTO;

import reactor.core.publisher.Mono;

@Configuration
public class PurchaseDetailRouter {

    @Bean
    public RouterFunction<ServerResponse> purchaseDetailRoutes(PurchaseDetailService purchaseDetailService){
        return route(POST("/purchases/{purchaseId}/details"), request-> createPurchaseDetail(request, purchaseDetailService));

    }

    public Mono<ServerResponse> createPurchaseDetail(ServerRequest request, PurchaseDetailService purchaseDetailService){
        UUID fkPurchase = UUID.fromString(request.pathVariable("purchaseId"));
        return request.bodyToMono(PurchaseDetailCreateDTO.class)
                        .flatMap(purchaseDetailDto-> purchaseDetailService.createPurchaseDetail(fkPurchase, purchaseDetailDto))
                        .flatMap(savedPurchaseDetail -> ServerResponse.ok().bodyValue(savedPurchaseDetail));
    }
    
    
}
