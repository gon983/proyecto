package com.proyect.mvp.infrastructure.routes;



import com.proyect.mvp.application.dtos.create.UserCreateDTO;
import com.proyect.mvp.application.dtos.requests.LoginRequest;
import com.proyect.mvp.application.dtos.update.UserUpdateDTO;
import com.proyect.mvp.application.services.UserService;
import com.proyect.mvp.domain.model.entities.UserEntity;
import com.proyect.mvp.infrastructure.security.CustomReactiveAuthenticationManager;
import com.proyect.mvp.infrastructure.security.UserAuthenticationDTO;

import com.proyect.mvp.infrastructure.security.handlers.JsonAuthenticationFailureHandler;
import com.proyect.mvp.infrastructure.security.handlers.JsonAuthenticationSuccessHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouter {
    

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserService userService, CustomReactiveAuthenticationManager authenticationManager, 
    JsonAuthenticationSuccessHandler successHandler, JsonAuthenticationFailureHandler failureHandler ) {
        return route(GET("/api/admin/users"), request -> getAllUsers(userService))
                .andRoute(GET("/api/admin/users/{id}"), request -> getUserById(request, userService))
                .andRoute(POST("/api/admin/users"), request -> createUser(request, userService))
                .andRoute(PUT("/api/admin/users/{id}"), request -> updateUser(request, userService))
                .andRoute(POST("/login"), request -> login(request, authenticationManager, successHandler, failureHandler))
                .andRoute(POST("/public/register"), request -> register(request, userService));
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

        private Mono<ServerResponse> login(ServerRequest request, CustomReactiveAuthenticationManager authenticationManager, JsonAuthenticationSuccessHandler successHandler, JsonAuthenticationFailureHandler failureHandler){
        return request.bodyToMono(LoginRequest.class)
            .doOnNext(dto -> dto.imprimir())
            .switchIfEmpty(Mono.error(new BadCredentialsException("Credentials cannot be empty")))
            .flatMap(loginRequest -> {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                );
                
                return authenticationManager.authenticate(authToken)
                    .flatMap(auth -> successHandler.createResponse(auth))
                    .onErrorResume(e -> failureHandler.createResponse(e));
            });

        }

        private Mono<ServerResponse> register(ServerRequest request, UserService userService){
            return request.bodyToMono(UserAuthenticationDTO.class)
                        .flatMap(dto-> userService.register(dto))
                        .flatMap(mono -> ServerResponse.ok().build());


        }
    }

