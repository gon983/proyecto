package com.proyect.mvp.infrastructure.routes;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.proyect.mvp.application.dtos.create.DefaultProductxCollectionPointxWeekCreateDTO;
import com.proyect.mvp.application.services.DefaultProductxCollectionPointxWeekService;
import com.proyect.mvp.domain.model.entities.DefaultProductxCollectionPointxWeekEntity;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.UUID;

@Configuration
public class DefaultProductxCollectionPointxWeekRouter {

    @Bean
    RouterFunction<ServerResponse> defaultProductxCpRoutes(DefaultProductxCollectionPointxWeekService defaultProductxCpService){
    return route(GET("/defaultProductsxCp/{idUser}"), request -> getDefaultProductsxCpxWeek(request, defaultProductxCpService))
                .andRoute(GET("/defaultProductsxCpToVote/{idCollectionPoint}"), request -> getDefaultProductsxCpToVote(request, defaultProductxCpService))
                .andRoute(GET("/renewalProductsManual/{collectionPointId}"), request -> renewalProductsManual(request, defaultProductxCpService));

}

    // Mono<ServerResponse> createDefaultProductxCp(ServerRequest request, DefaultProductxCollectionPointxWeekService defaultProductxCpService){
    //     return request.bodyToMono(DefaultProductxCollectionPointxWeekCreateDTO.class)
    //                    .flatMap(dto -> defaultProductxCpService.createDefaultProductxCollectionPointxWeek(dto))
    //                    .flatMap(saved -> ServerResponse.ok().bodyValue(saved));
    // } En teoria este endpoint no se tiene q usar, lo hice para probar las purchases

    Mono<ServerResponse> getDefaultProductsxCpxWeek(ServerRequest request, DefaultProductxCollectionPointxWeekService defaultProductxCpService){
        UUID idUser = UUID.fromString(request.pathVariable("idUser"));
        return defaultProductxCpService.getAllProductsForCpWithLevelPrice(idUser)
            .collectList()
            .flatMap(productList -> ServerResponse.ok().bodyValue(productList));
    }

    Mono<ServerResponse> getDefaultProductsxCpToVote(ServerRequest request, DefaultProductxCollectionPointxWeekService defaultProductxCpService){
        UUID idCollectionPoint = UUID.fromString(request.pathVariable("idCollectionPoint"));
        return defaultProductxCpService.getAllDefaultProductsxCpxWeekToVote(idCollectionPoint)
                                        .collectList()
                                        .flatMap(productList -> ServerResponse.ok().bodyValue(productList));
    }

    Mono<ServerResponse> renewalProductsManual(ServerRequest request, DefaultProductxCollectionPointxWeekService defaultProductxCollectionPointxWeekService) {
        UUID collectionPointId = UUID.fromString(request.pathVariable("collectionPointId"));
        return Mono.fromRunnable(() -> defaultProductxCollectionPointxWeekService.renewProductsForCollectionPoint(collectionPointId))
                .then(ServerResponse.ok().build());
    }
    
            
    }

        