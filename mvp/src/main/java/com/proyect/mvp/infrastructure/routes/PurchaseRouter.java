package com.proyect.mvp.infrastructure.routes;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.net.HttpStatus;
import com.proyect.mvp.application.dtos.create.PurchaseCreateDTO;
import com.proyect.mvp.application.dtos.other.MercadoPagoNotificationDTO;
import com.proyect.mvp.application.dtos.requests.ReceivePurchaseDTO;
import com.proyect.mvp.application.services.PurchaseService;
import com.proyect.mvp.infrastructure.config.middlewares.ConfirmPurchaseMiddleware;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PurchaseRouter {

    @Bean
    public RouterFunction<ServerResponse> purchaseRoutes(PurchaseService purchaseService, ConfirmPurchaseMiddleware confirmPurchaseMiddleware) {
        return route(POST("/api/user/purchases"), request -> createPurchase(request, purchaseService))
                .andRoute(GET("/api/user/purchases/{idPurchase}"), request -> getPurchaseWithDetails(request, purchaseService))
                .andRoute(POST("/api/user/confirmPurchase/{idPurchase}"), request -> confirmPurchase(request, purchaseService))
                .andRoute(POST("/confirmPayment"), request ->  confirmPurchaseMiddleware.validate(request)
                                                                        .flatMap(valid -> valid ? confirmPayment(request, purchaseService) : ServerResponse.status(401).build()))
                .andRoute(POST("/api/user/receivePurchase/{idPurchase}"), request -> receivePurchase(request, purchaseService))
                // Añadir esta ruta en el método purchaseRoutes
                .andRoute(GET("/api/user/cart"), request -> getActiveCart(request, purchaseService));
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
        String cachedBody = ConfirmPurchaseMiddleware.getCachedBody(request);
        
        if (cachedBody != null) {
            System.out.println("Usando cuerpo cacheado: " + cachedBody);
            
            try {
                ObjectMapper mapper = new ObjectMapper();
                MercadoPagoNotificationDTO notification = mapper.readValue(cachedBody, MercadoPagoNotificationDTO.class);
                
                System.out.println("Notificación deserializada: " + notification);
                System.out.println("Tipo: " + notification.getType());
                System.out.println("Data ID: " + notification.getData().getId());
                
                return purchaseService.procesarNotificacionPago(notification.getType(), notification.getData().getId())
                    .flatMap(confirmedPurchase -> ServerResponse.ok().build())
                    .onErrorResume(e -> {
                        System.err.println("Error procesando la notificación: " + e.getMessage());
                        e.printStackTrace();
                        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    });
            } catch (Exception e) {
                System.err.println("Error deserializando JSON: " + e.getMessage());
                e.printStackTrace();
                return ServerResponse.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            System.err.println("No se encontró el cuerpo cacheado");
            return ServerResponse.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private Mono<ServerResponse> receivePurchase(ServerRequest request, PurchaseService purchaseService){
        UUID idPurchase = UUID.fromString(request.pathVariable("idPurchase"));

        return request.bodyToMono(ReceivePurchaseDTO.class)
                      .flatMap(purchase-> purchaseService.receivePurchase(idPurchase, purchase))
                      .flatMap(details-> ServerResponse.ok().bodyValue(details));

    }

    private Mono<ServerResponse> getActiveCart(ServerRequest request, PurchaseService purchaseService) {
        // Extraer el ID del usuario del token o parámetro
        UUID userId = UUID.fromString(request.queryParam("userId").orElseThrow());
        return purchaseService.getUserActiveCart(userId)
                .flatMap(cart -> ServerResponse.ok().bodyValue(cart))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
    
}
