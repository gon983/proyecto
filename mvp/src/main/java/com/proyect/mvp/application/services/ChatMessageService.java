package com.proyect.mvp.application.services;



import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyect.mvp.application.dtos.create.ChatMessageCreateDTO;
import com.proyect.mvp.domain.model.entities.ChatMessageEntity;

import com.proyect.mvp.domain.repository.ChatMessageRepository;
import com.proyect.mvp.domain.repository.DeviceTokenRepository;
import com.proyect.mvp.domain.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final FireBaseNotificationService firebaseNotificationService;

    public ChatMessageService(
            ChatMessageRepository chatMessageRepository,
            UserRepository userRepository,
            DeviceTokenRepository deviceTokenRepository,
            FireBaseNotificationService firebaseNotificationService) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.deviceTokenRepository = deviceTokenRepository;
        this.firebaseNotificationService = firebaseNotificationService;
    }

    public Flux<ChatMessageEntity> getUserMessages(UUID userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
                .thenMany(chatMessageRepository.findByUserIdOrderBySentAtAsc(userId));
    }

    public Mono<ChatMessageEntity> saveUserMessage(UUID userId, ChatMessageCreateDTO messageDTO) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
                .flatMap(user -> {
                    ChatMessageEntity message = ChatMessageEntity.builder()
                            .userId(userId)
                            .isFromCompany(false)
                            .content(messageDTO.getContent())
                            .sentAt(ZonedDateTime.now())
                            .read(false)
                            .build();
                    
                    return chatMessageRepository.save(message);
                });
    }

    public Mono<ChatMessageEntity> saveCompanyMessage(ChatMessageCreateDTO messageDTO) {
    return userRepository.findById(messageDTO.getUserId())
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
            .flatMap(user -> {
                ChatMessageEntity message = ChatMessageEntity.builder()
                        .userId(messageDTO.getUserId())
                        .isFromCompany(true)
                        .content(messageDTO.getContent())
                        .sentAt(ZonedDateTime.now())
                        .read(false)
                        .build();
                
                return chatMessageRepository.save(message)
                        .flatMap(savedMessage -> 
                            deviceTokenRepository.findByUserId(messageDTO.getUserId())
                                    .flatMap(token -> 
                                        // Wrap blocking call in Mono and handle errors
                                        Mono.fromRunnable(() -> 
                                            firebaseNotificationService.sendChatNotification(
                                                token.getDeviceToken(), 
                                                "Nuevo mensaje de la empresa"
                                            )
                                        )
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .onErrorResume(e -> {
                                            // Log error and continue
                                            return Mono.empty();
                                        })
                                    )
                                    .then(Mono.just(savedMessage))
                        );
            });
}

    public Mono<ChatMessageEntity> markMessageAsRead(UUID messageId) {
        return chatMessageRepository.findById(messageId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensaje no encontrado")))
                .flatMap(message -> {
                    ChatMessageEntity updatedMessage = ChatMessageEntity.builder()
                            .idMessage(message.getIdMessage())
                            .userId(message.getUserId())
                            .isFromCompany(message.isFromCompany())
                            .content(message.getContent())
                            .sentAt(message.getSentAt())
                            .read(true)
                            .build();
                    
                    return chatMessageRepository.save(updatedMessage);
                });
    }
} 