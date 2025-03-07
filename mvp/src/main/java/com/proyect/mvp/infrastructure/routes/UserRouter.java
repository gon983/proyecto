package com.proyect.mvp.infrastructure.routes;



import com.proyect.mvp.application.dtos.create.UserCreateDTO;
import com.proyect.mvp.application.dtos.update.UserUpdateDTO;
import com.proyect.mvp.application.services.UserService;
import com.proyect.mvp.domain.model.entities.UserEntity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserService userService) {
        return route(GET("/users"), request -> getAllUsers(userService))
                .andRoute(GET("/users/{id}"), request -> getUserById(request, userService))
                .andRoute(POST("/users"), request -> createUser(request, userService))
                .andRoute(PUT("/users/{id}"), request -> updateUser(request, userService));
    }

    private Mono<ServerResponse> getAllUsers(UserService userService) {
        return ServerResponse.ok().body(userService.getAllUsers(), UserEntity.class);
    }

    private Mono<ServerResponse> getUserById(ServerRequest request, UserService userService) {
        try {
            UUID id = UUID.fromString(request.pathVariable("id"));
            return userService.getUserById(id)
                    .flatMap(user -> ServerResponse.ok().bodyValue(user));
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
        }
    }

    private Mono<ServerResponse> createUser(ServerRequest request, UserService userService) {
        return request.bodyToMono(UserCreateDTO.class)
                .flatMap(user -> userService.saveNewUser(user))
                .flatMap(savedUser -> ServerResponse.ok().bodyValue(savedUser))
                .onErrorResume(ResponseStatusException.class, e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> updateUser(ServerRequest request, UserService userService) {
        try {
            UUID id = UUID.fromString(request.pathVariable("id"));
            return request.bodyToMono(UserUpdateDTO.class)
                    .flatMap(user -> userService.updateUser(id, user))
                    .flatMap(updatedUser -> ServerResponse.ok().bodyValue(updatedUser));
        } catch (IllegalArgumentException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format"));
        }
    }
}

