package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.dtos.create.PurchaseCreateDTO;
import com.proyect.mvp.application.dtos.other.MercadoPagoNotificationDTO;
import com.proyect.mvp.application.dtos.requests.PurchaseCreationRequest;
import com.proyect.mvp.application.services.MercadoPagoSplitService;
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.infrastructure.config.middlewares.ConfirmPurchaseMiddleware;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.net.HttpStatus;
import com.mercadopago.resources.preference.Preference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RequestPredicates;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Configuration
public class MercadoPagoSplitRouter {

    @Bean
    public RouterFunction<ServerResponse> mercadoPagoSplitRoutes(MercadoPagoSplitService mercadoPagoSplitService, ConfirmPurchaseMiddleware confirmPurchaseMiddleware) {
        return RouterFunctions
            .route(RequestPredicates.GET("/mp/auth/url/{productorId}"), request -> generarUrlAutorizacion(request, mercadoPagoSplitService))
            .andRoute(RequestPredicates.GET("/mp/auth/callback"), request -> procesarAutorizacion(request, mercadoPagoSplitService))
            .andRoute(RequestPredicates.POST("/mp/preference/{purchaseId}"), request -> crearPreferencia(request, mercadoPagoSplitService))
            .andRoute(RequestPredicates.POST("/mp/notification"), request -> confirmPurchaseMiddleware.validate(request)
                                                                            .flatMap(valid -> valid ? procesarNotificacion(request, mercadoPagoSplitService): ServerResponse.status(401).build()))
            // .andRoute(RequestPredicates.POST("/mp/purchase/complete"), request -> crearCompraCompletaRoute(request, mercadoPagoSplitService))
            .andRoute(RequestPredicates.GET("mp/confirmPurchase/{purchaseId}") , request -> confirmarCompra(request, mercadoPagoSplitService))
            .andRoute(RequestPredicates.POST("/mp/refresh/token/{productorId}"), request -> refrescarToken(request, mercadoPagoSplitService));
    }

    private Mono<ServerResponse> generarUrlAutorizacion(ServerRequest request, MercadoPagoSplitService mercadoPagoSplitService) {
        UUID productorId = UUID.fromString(request.pathVariable("productorId"));
        return ServerResponse.ok().bodyValue(mercadoPagoSplitService.generarUrlAutorizacionProductor(productorId));
    }

    private Mono<ServerResponse> procesarAutorizacion(ServerRequest request, MercadoPagoSplitService mercadoPagoSplitService) {
        System.out.println("Entre maldicion");
        Mono<String> codeMono = Mono.justOrEmpty(request.queryParam("code"));
        Mono<String> stateMono = Mono.justOrEmpty(request.queryParam("state"));
    
        return Mono.zip(codeMono, stateMono)
            .flatMap(tuple -> {
                String code = tuple.getT1();
                String state = tuple.getT2();
                try {
                    UUID productorId = UUID.fromString(state);
                    return mercadoPagoSplitService.procesarAutorizacionProductor(code, productorId)
                        .then(ServerResponse.ok().build());
                } catch (IllegalArgumentException e) {
                    return ServerResponse.badRequest().bodyValue("Invalid state parameter");
                }
            })
            .switchIfEmpty(ServerResponse.badRequest().bodyValue("Missing code or state parameter"));
    }
    private Mono<ServerResponse> crearPreferencia(ServerRequest request, MercadoPagoSplitService mercadoPagoSplitService) {
        UUID purchaseId = UUID.fromString(request.pathVariable("purchaseId"));
        return mercadoPagoSplitService.crearPreferenciaPago(purchaseId)
            .flatMap(preference -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(preference));
    }

    private Mono<ServerResponse> procesarNotificacion(ServerRequest request, MercadoPagoSplitService mercadoPagoSplitService) {

        String cachedBody = ConfirmPurchaseMiddleware.getCachedBody(request);
        
        if (cachedBody != null) {
            System.out.println("Usando cuerpo cacheado: " + cachedBody);
            
            try {
                ObjectMapper mapper = new ObjectMapper();
                MercadoPagoNotificationDTO notification = mapper.readValue(cachedBody, MercadoPagoNotificationDTO.class);
                
                System.out.println("Notificación deserializada: " + notification);
                System.out.println("Tipo: " + notification.getType());
                System.out.println("Data ID: " + notification.getData().getId());
                
                return mercadoPagoSplitService.procesarNotificacionPago(notification.getType(), notification.getData().getId())
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
   
    

    // private Mono<ServerResponse> crearCompraCompletaRoute(ServerRequest request, MercadoPagoSplitService mercadoPagoSplitService) {
    //     return request.bodyToMono(PurchaseCreationRequest.class)
    //         .flatMap(creationRequest ->
    //             mercadoPagoSplitService.crearCompraCompleta(creationRequest.getPurchaseDto(), creationRequest.getDetails())
    //                 .flatMap(preference -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(preference)));
    // }

    private Mono<ServerResponse> refrescarToken(ServerRequest request, MercadoPagoSplitService mercadoPagoSplitService) {
        UUID productorId = UUID.fromString(request.pathVariable("productorId"));
        return mercadoPagoSplitService.refrescarTokenProductor(productorId)
            .flatMap(token -> ServerResponse.ok().bodyValue(token));
    }

    private Mono<ServerResponse> confirmarCompra(ServerRequest request, MercadoPagoSplitService mercadoPagoSplitService){
        UUID purchaseId = UUID.fromString(request.pathVariable("purchaseId"));
        return mercadoPagoSplitService.crearPreferenciaPago(purchaseId)
                                      .flatMap(preference -> ServerResponse.ok().bodyValue(preference));
    }

}
