package com.proyect.mvp.infrastructure.routes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.proyect.mvp.application.services.StockMovementService;
import com.proyect.mvp.dtos.create.StockMovementCreateDTO;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.UUID;

@Configuration
public class StockMovementRouter {

    @Bean
    RouterFunction<ServerResponse>  stockMovementroutes(StockMovementService stockMovementService){
        return route(POST("/stockMovement/{idUser}"), request -> createStockMovement(request, stockMovementService));

    }

    Mono<ServerResponse> createStockMovement(ServerRequest request, StockMovementService stockMovementService) {
        UUID idUser = UUID.fromString(request.pathVariable("idUser"));
        return request.bodyToMono(StockMovementCreateDTO.class)
            .flatMap(dto -> stockMovementService.registerMovement(idUser, dto) // No usar bodyValue con Mono<Void>
                .then(ServerResponse.noContent().build()) // ✅ Responde con 204 No Content
            );
    }
    


    
}
