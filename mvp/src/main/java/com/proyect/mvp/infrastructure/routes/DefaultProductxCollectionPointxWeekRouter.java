package com.proyect.mvp.infrastructure.routes;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.proyect.mvp.application.services.DefaultProductxCollectionPointxWeekService;
import com.proyect.mvp.domain.model.entities.DefaultProductxCollectionPointxWeekEntity;
import com.proyect.mvp.dtos.create.DefaultProductxCollectionPointxWeekCreateDTO;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.UUID;

@Configuration
public class DefaultProductxCollectionPointxWeekRouter {

    @Bean
    RouterFunction<ServerResponse> defaultProductxCpRoutes(DefaultProductxCollectionPointxWeekService defaultProductxCpService){
    return route(POST("/defaultProductsxCp"), request -> createDefaultProductxCp(request, defaultProductxCpService))
            .andRoute(GET("/defaultProductsxCp/{idUser}"), request -> getDefaultProductsxCpxWeek(request, defaultProductxCpService));

}

    Mono<ServerResponse> createDefaultProductxCp(ServerRequest request, DefaultProductxCollectionPointxWeekService defaultProductxCpService){
        return request.bodyToMono(DefaultProductxCollectionPointxWeekCreateDTO.class)
                       .flatMap(dto -> defaultProductxCpService.createDefaultProductxCollectionPointxWeek(dto))
                       .flatMap(saved -> ServerResponse.ok().bodyValue(saved));
    }

    Mono<ServerResponse> getDefaultProductsxCpxWeek(ServerRequest request, DefaultProductxCollectionPointxWeekService defaultProductxCpService){
        UUID idUser = UUID.fromString(request.pathVariable("idUser"));
        return ServerResponse.ok().bodyValue(defaultProductxCpService.getAllProductsForCpWithLevelPrice(idUser));
    }

}