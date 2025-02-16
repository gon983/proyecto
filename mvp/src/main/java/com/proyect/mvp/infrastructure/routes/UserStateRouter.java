package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.application.services.UserStateService;
import com.proyect.mvp.domain.model.entities.UserStateEntity;
import com.proyect.mvp.dtos.create.UserStateCreateDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserStateRouter {

    @Bean
    public RouterFunction<ServerResponse> userStateRoutes(UserStateService userStateService) {
        return route(POST("/userStates"), request -> createUserState(request, userStateService))
                .andRoute(GET("/userStates"), request -> getAllUserStates(userStateService))
                .andRoute(GET("/userStates/{id}"), request -> getUserStateById(request, userStateService))
                .andRoute(DELETE("/userStates/{id}"), request -> deleteUserStateById(request, userStateService));
    }

    private Mono<ServerResponse> createUserState(ServerRequest request, UserStateService userStateService) {
        return request.bodyToMono(UserStateCreateDTO.class)
                .flatMap(userState -> userStateService.saveNewUserState(userState))
                .flatMap(savedUserState -> ServerResponse.ok().bodyValue(savedUserState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> getAllUserStates(UserStateService userStateService) {
        return ServerResponse.ok().body(userStateService.getAllUserStates(), UserStateEntity.class);
    }

    private Mono<ServerResponse> getUserStateById(ServerRequest request, UserStateService userStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return userStateService.getUserStateById(id)
                .flatMap(userState -> ServerResponse.ok().bodyValue(userState))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> deleteUserStateById(ServerRequest request, UserStateService userStateService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return userStateService.deleteUserStateById(id)
                .flatMap(v -> ServerResponse.noContent().build())
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }
}
