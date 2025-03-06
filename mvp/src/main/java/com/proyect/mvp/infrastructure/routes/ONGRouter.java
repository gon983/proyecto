package com.proyect.mvp.infrastructure.routes;



import com.proyect.mvp.application.services.ONGService;
import com.proyect.mvp.domain.model.dtos.create.ONGCreateDTO;
import com.proyect.mvp.domain.model.dtos.update.ONGUpdateDTO;
import com.proyect.mvp.domain.model.entities.ONGEntity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ONGRouter {

    @Bean
    public RouterFunction<ServerResponse> ongRoutes(ONGService ongService) {
        return org.springframework.web.reactive.function.server.RouterFunctions.route()
                .POST("/ongs",
                        req -> req.bodyToMono(ONGCreateDTO.class)
                                .flatMap(ongDTO -> {
                                    ONGEntity ong = ONGEntity.builder()
                                            .idOng(UUID.randomUUID())
                                            .name(ongDTO.getName())
                                            .account(ongDTO.getAccount())
                                            .build();
                                    return ongService.createONG(ong);
                                })
                                .flatMap(ong -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(ong)))
                .GET("/ongs",
                        req -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .body(ongService.getAllONGs(), ONGEntity.class))
                .GET("/ongs/{id}",
                        req -> ongService.getONG(UUID.fromString(req.pathVariable("id")))
                                .flatMap(ong -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(ong)))
                .PUT("/ongs",
                        req -> req.bodyToMono(ONGUpdateDTO.class)
                                .flatMap(ongService::updateONG)
                                .flatMap(ong -> ServerResponse.ok().build()))
                .build();
    }
}
