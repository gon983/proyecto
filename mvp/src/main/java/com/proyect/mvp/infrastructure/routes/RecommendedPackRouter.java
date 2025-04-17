package com.proyect.mvp.infrastructure.routes;


import com.proyect.mvp.application.dtos.create.RecommendedPackCreateDTO;
import com.proyect.mvp.application.dtos.update.PackProductAddDTO;
import com.proyect.mvp.application.dtos.update.PackProductDeleteDTO;
import com.proyect.mvp.application.dtos.update.PackProductEditDTO;
import com.proyect.mvp.application.services.RecommendedPackService;
import com.proyect.mvp.domain.model.entities.RecommendedPackEntity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RecommendedPackRouter {

    @Bean
    public RouterFunction<ServerResponse> recommendedPackRoutes(RecommendedPackService packService) {
        return route(GET("/public/recommended-packs"), request -> getAllPacks(packService))
                .andRoute(GET("/public/recommended-pack/{id}"), request -> getPackById(request, packService))
                .andRoute(POST("/api/admin/recommended-packs-crear"), request -> createPack(request, packService))
                .andRoute(PUT("/api/admin/recommended-packs-editar"), request -> editarPack(request,packService))
                .andRoute(POST("/api/admin/recommended-packs-agregar-productos"), request -> agregarProductos(request, packService))
                .andRoute(POST("/api/admin/recommended-packs-eliminar-productos"), request -> eliminarProductos(request, packService));
    }

    private Mono<ServerResponse> getAllPacks(RecommendedPackService packService) {
        return packService.getAllPacksWithProducts().collectList().flatMap(pack-> ServerResponse.ok().bodyValue(pack));
    }

    private Mono<ServerResponse> getPackById(ServerRequest request, RecommendedPackService packService) {
        try {
            UUID id = UUID.fromString(request.pathVariable("id"));
            return packService.getPackWithProducts(id)
                    .flatMap(pack -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(pack));
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
        }
    }

    private Mono<ServerResponse> createPack(ServerRequest request, RecommendedPackService packService) {
        return request.bodyToMono(RecommendedPackCreateDTO.class)
                .flatMap(packService::createPack)
                .flatMap(createdPack -> ServerResponse.ok().build())
                .onErrorResume(ResponseStatusException.class,
                        e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> editarPack(ServerRequest request, RecommendedPackService packService) {
        return request.bodyToMono(PackProductEditDTO.class)
                .flatMap(packService::editarPack)
                .flatMap(createdPack -> ServerResponse.ok().build())
                .onErrorResume(ResponseStatusException.class,
                        e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> agregarProductos(ServerRequest request, RecommendedPackService packService) {
        return request.bodyToMono(PackProductAddDTO.class)
                .flatMap(packService::agregarProductosAlPack)
                .flatMap(createdPack -> ServerResponse.ok().build())
                .onErrorResume(ResponseStatusException.class,
                        e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }


    private Mono<ServerResponse> eliminarProductos(ServerRequest request, RecommendedPackService packService) {
        return request.bodyToMono(PackProductDeleteDTO.class)
                .flatMap(packService::quitarProductoDelPack)
                .flatMap(createdPack -> ServerResponse.ok().build())
                .onErrorResume(ResponseStatusException.class,
                        e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }





}
