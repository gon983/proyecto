package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.dtos.create.PurchaseCreateDTO;
import com.proyect.mvp.application.dtos.requests.PurchaseCreationRequest;
import com.proyect.mvp.application.services.MercadoPagoSplitService;
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
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
    public RouterFunction<ServerResponse> mercadoPagoSplitRoutes(MercadoPagoSplitService mercadoPagoSplitService) {
        return RouterFunctions
            .route(RequestPredicates.GET("/mp/auth/url"), request -> generarUrlAutorizacion(mercadoPagoSplitService))
            .andRoute(RequestPredicates.POST("/mp/auth/callback"), request -> procesarAutorizacion(request, mercadoPagoSplitService))
            .andRoute(RequestPredicates.POST("/mp/preference/{purchaseId}"), request -> crearPreferencia(request, mercadoPagoSplitService))
            .andRoute(RequestPredicates.POST("/mp/notification"), request -> procesarNotificacion(request, mercadoPagoSplitService))
            .andRoute(RequestPredicates.POST("/mp/purchase/complete"), request -> crearCompraCompletaRoute(request, mercadoPagoSplitService))
            .andRoute(RequestPredicates.POST("/mp/refresh/token/{productorId}"), request -> refrescarToken(request, mercadoPagoSplitService));
    }

    private Mono<ServerResponse> generarUrlAutorizacion(MercadoPagoSplitService mercadoPagoSplitService) {
        return ServerResponse.ok().bodyValue(mercadoPagoSplitService.generarUrlAutorizacionProductor());
    }

    private Mono<ServerResponse> procesarAutorizacion(ServerRequest request, MercadoPagoSplitService mercadoPagoSplitService) {
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
        Mono<String> typeMono = Mono.justOrEmpty(request.queryParam("type"));
        Mono<String> dataIdMono = Mono.justOrEmpty(request.queryParam("id"));
        return Mono.zip(typeMono, dataIdMono)
                    .flatMap(tuple ->{
                        String type = tuple.getT1();
                        String dataId = tuple.getT2();
                        
                        return mercadoPagoSplitService.procesarNotificacionPago(type, dataId)
                        .then(ServerResponse.ok().build());
                    });
   
    }

    private Mono<ServerResponse> crearCompraCompletaRoute(ServerRequest request, MercadoPagoSplitService mercadoPagoSplitService) {
        return request.bodyToMono(PurchaseCreationRequest.class)
            .flatMap(creationRequest ->
                mercadoPagoSplitService.crearCompraCompleta(creationRequest.getPurchaseDto(), creationRequest.getDetails())
                    .flatMap(preference -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(preference)));
    }

    private Mono<ServerResponse> refrescarToken(ServerRequest request, MercadoPagoSplitService mercadoPagoSplitService) {
        UUID productorId = UUID.fromString(request.pathVariable("productorId"));
        return mercadoPagoSplitService.refrescarTokenProductor(productorId)
            .flatMap(token -> ServerResponse.ok().bodyValue(token));
    }
}

