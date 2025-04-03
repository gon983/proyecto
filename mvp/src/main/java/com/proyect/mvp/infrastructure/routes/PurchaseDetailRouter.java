package com.proyect.mvp.infrastructure.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import com.proyect.mvp.application.dtos.create.PurchaseDetailCreateDTO;
import com.proyect.mvp.application.dtos.requests.ProductsPayedDTO;
import com.proyect.mvp.application.dtos.update.PurchaseDetailUpdateDTO;
import com.proyect.mvp.application.services.PurchaseDetailService;


import reactor.core.publisher.Mono;

@Configuration
public class PurchaseDetailRouter {

    @Bean
    public RouterFunction<ServerResponse> purchaseDetailRoutes(PurchaseDetailService purchaseDetailService){
        return route(POST("/api/user/purchases/details/{purchaseId}"), request-> createPurchaseDetail(request, purchaseDetailService))
        .andRoute(PUT("/api/user/purchases/details/{detailId}"), request -> updatePurchaseDetail(request, purchaseDetailService));


    }

    public Mono<ServerResponse> createPurchaseDetail(ServerRequest request, PurchaseDetailService purchaseDetailService){
        UUID fkPurchase = UUID.fromString(request.pathVariable("purchaseId"));
        UUID fkBuyer = UUID.fromString(request.pathVariable("idUser"));
        return request.bodyToMono(PurchaseDetailCreateDTO.class)
                        .flatMap(purchaseDetailDto-> purchaseDetailService.createPurchaseDetail(fkBuyer,  fkPurchase, purchaseDetailDto))
                        .flatMap(savedPurchaseDetail -> ServerResponse.ok().bodyValue(savedPurchaseDetail));
    }

    private Mono<ServerResponse> updatePurchaseDetail(ServerRequest request, PurchaseDetailService purchaseDetailService) {
        UUID detailId = UUID.fromString(request.pathVariable("detailId"));
        return request.bodyToMono(PurchaseDetailUpdateDTO.class)
                    .flatMap(updateDto -> purchaseDetailService.updatePurchaseDetail(detailId, updateDto))
                    .flatMap(updatedDetail -> ServerResponse.ok().bodyValue(updatedDetail));
    }
   
    





   

    
    }
    
    

