package com.proyect.mvp.infrastructure.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
import com.proyect.mvp.infrastructure.exception.PurchaseDetailNotInPendingStateException;
import com.proyect.mvp.infrastructure.security.UserContextService;

import reactor.core.publisher.Mono;

@Configuration
public class PurchaseDetailRouter {

    @Bean
    public RouterFunction<ServerResponse> purchaseDetailRoutes(PurchaseDetailService purchaseDetailService, UserContextService userContext){
        return route(POST("/api/user/purchases/details/{purchaseId}"), request-> createPurchaseDetail(request, purchaseDetailService, userContext))
        .andRoute(PUT("/api/user/purchases/details/{detailId}"), request -> updatePurchaseDetail(request, purchaseDetailService))
        .andRoute(DELETE("/api/user/purchases/details/{detailId}"), request -> deleteDetailWhenBuying(request, purchaseDetailService));


    }

    public Mono<ServerResponse> createPurchaseDetail(ServerRequest request, PurchaseDetailService purchaseDetailService, UserContextService userContext){
        UUID fkPurchase = UUID.fromString(request.pathVariable("purchaseId"));
        return userContext.getCurrentIdUser().flatMap(fkBuyer -> {
        return request.bodyToMono(PurchaseDetailCreateDTO.class)
                        .flatMap(purchaseDetailDto-> purchaseDetailService.createPurchaseDetail(UUID.fromString(fkBuyer),  fkPurchase, purchaseDetailDto))
                        .flatMap(savedPurchaseDetail -> ServerResponse.ok().bodyValue(savedPurchaseDetail));});
    }

    private Mono<ServerResponse> updatePurchaseDetail(ServerRequest request, PurchaseDetailService purchaseDetailService) {
        UUID detailId = UUID.fromString(request.pathVariable("detailId"));
        return request.bodyToMono(PurchaseDetailUpdateDTO.class)
                    .flatMap(updateDto -> purchaseDetailService.updatePurchaseDetail(detailId, updateDto))
                    .flatMap(updatedDetail -> ServerResponse.ok().build());
    }

   
    private Mono<ServerResponse> deleteDetailWhenBuying(ServerRequest request, PurchaseDetailService purchaseDetailService) {
        UUID detailId = UUID.fromString(request.pathVariable("detailId"));
        
        return purchaseDetailService.deleteDetailWhenBuying(detailId)
                .then(ServerResponse.noContent().build()) // 204 No Content on successful deletion
                .onErrorResume(PurchaseDetailNotInPendingStateException.class,
                        e -> ServerResponse.badRequest().bodyValue(e.getMessage())) // 400 Bad Request with error message
                .onErrorResume(Exception.class, // Catch any other potential exceptions
                        e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .bodyValue("Error interno del servidor: " + e.getMessage())); // 500 Internal Server Error
    }
    





   

    
    }
    
    

