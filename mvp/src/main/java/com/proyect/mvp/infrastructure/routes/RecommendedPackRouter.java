package com.proyect.mvp.infrastructure.routes;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.UUID;

import com.proyect.mvp.application.dtos.create.RecommendedPackCreateDTO;
import com.proyect.mvp.application.services.RecommendedPackService;
import com.proyect.mvp.domain.model.entities.RecommendedPackEntity;

@Configuration
public class RecommendedPackRouter {
    
    @Bean
    public RouterFunction<ServerResponse> recommendedPackRoutes(RecommendedPackService packService) {
        return route(GET("/public/recommended-packs"), 
                req -> ServerResponse.ok().bodyValue(packService.getAllPacks()))
            .andRoute(GET("/public/recommended-packs/{id}"), 
                req -> {
                    UUID id = UUID.fromString(req.pathVariable("id"));
                    return ServerResponse.ok().bodyValue(packService.getPackWithProducts(id));
                })
                .andRoute(POST("/api/admin/recommended-packs"), 
                request -> request.bodyToMono(RecommendedPackCreateDTO.class)
                    .flatMap(packService::createPack)
                    .flatMap(createdPack -> ServerResponse.ok().bodyValue(createdPack))
            );
            
    }
}