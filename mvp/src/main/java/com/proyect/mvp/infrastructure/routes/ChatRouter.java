package com.proyect.mvp.infrastructure.routes;



import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.proyect.mvp.application.dtos.create.ChatMessageCreateDTO;
import com.proyect.mvp.application.dtos.create.DeviceTokenCreateDTO;
import com.proyect.mvp.application.services.ChatMessageService;
import com.proyect.mvp.application.services.DeviceTokenService;
import com.proyect.mvp.domain.model.entities.ChatMessageEntity;
import com.proyect.mvp.domain.model.entities.DeviceTokenEntity;
import com.proyect.mvp.infrastructure.security.UserContextService;

import reactor.core.publisher.Mono;

@Configuration 
public class ChatRouter {
    
    @Bean
    public RouterFunction<ServerResponse> chatRoutes(ChatMessageService chatMessageService, DeviceTokenService deviceTokenService, UserContextService userContext) {
        return route(GET("/api/user/chat/messages"), request -> getUserMessages(request, chatMessageService, userContext))
                .andRoute(GET("/api/admin/chat/get-user-messages/{idUser}"), request -> getUserMessagesByAdmin(request, chatMessageService))
                .andRoute(POST("/api/user/chat/user-message"), request -> createUserMessage(request, chatMessageService, userContext))
                .andRoute(POST("/api/admin/chat/company-message"), request -> createCompanyMessage(request, chatMessageService))
                .andRoute(POST("/api/admin/chat/mark-read/{userId}"), request -> markUserAsRead(request, chatMessageService))
                // .andRoute(POST("/api/user/chat/mark-read"), request -> markFromUserAsRead(request, chatMessageService))
                .andRoute(POST("/api/user/chat/register-token"), request -> registerDeviceToken(request, deviceTokenService))
                .andRoute(DELETE("/api/admin/chat/unregister-token/{deviceType}"), request -> unregisterDeviceToken(request, deviceTokenService, userContext));
    }

    private Mono<ServerResponse> getUserMessages(ServerRequest request, ChatMessageService chatMessageService, UserContextService userContext) {
        
        return userContext.getCurrentIdUser().flatMap(userId ->        
        ServerResponse.ok().body(chatMessageService.getUserMessages(UUID.fromString(userId)), ChatMessageEntity.class));
    }

    private Mono<ServerResponse> getUserMessagesByAdmin(ServerRequest request, ChatMessageService chatMessageService) {
        String userId = request.pathVariable("idUser");
        return ServerResponse.ok().body(chatMessageService.getUserMessages(UUID.fromString(userId)), ChatMessageEntity.class);
    }

    private Mono<ServerResponse> createUserMessage(ServerRequest request, ChatMessageService chatMessageService, UserContextService userContext) {
        return userContext.getCurrentIdUser().flatMap(userId ->  
        request.bodyToMono(ChatMessageCreateDTO.class)
                .flatMap(message -> chatMessageService.saveUserMessage(UUID.fromString(userId), message))
                .flatMap(savedMessage -> ServerResponse.ok().bodyValue(savedMessage)));
    }

    private Mono<ServerResponse> createCompanyMessage(ServerRequest request, ChatMessageService chatMessageService) {
        return request.bodyToMono(ChatMessageCreateDTO.class)
                .flatMap(message -> chatMessageService.saveCompanyMessage(message))
                .flatMap(savedMessage -> ServerResponse.ok().bodyValue(savedMessage));
    }

    private Mono<ServerResponse> markUserAsRead(ServerRequest request, ChatMessageService chatMessageService) {
        UUID idUser = UUID.fromString(request.pathVariable("userId"));
        return chatMessageService.markUserMessageAsRead(idUser)
                .flatMap(message -> ServerResponse.ok().build());
    }

    private Mono<ServerResponse> registerDeviceToken(ServerRequest request, DeviceTokenService deviceTokenService) {
        return request.bodyToMono(DeviceTokenCreateDTO.class)
                .flatMap(token -> deviceTokenService.saveDeviceToken(token))
                .flatMap(savedToken -> ServerResponse.ok().bodyValue(savedToken));
    }

    private Mono<ServerResponse> unregisterDeviceToken(ServerRequest request, DeviceTokenService deviceTokenService,UserContextService userContext) {
        
        String deviceType = request.pathVariable("deviceType");
        
        
        return userContext.getCurrentIdUser().flatMap(userId ->  
        deviceTokenService.deleteDeviceToken(UUID.fromString(userId), deviceType)
                .then(ServerResponse.ok().build()));
    }
}