package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyect.mvp.domain.model.entities.ChatMessageEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ChatMessageRepository extends R2dbcRepository<ChatMessageEntity, UUID> {
    Flux<ChatMessageEntity> findByUserIdOrderBySentAtAsc(UUID userId);

    @Query("UPDATE chat-messages SET read = true WHERE user_id = :idUser AND is_from_company = :false ")
    Mono<Void> markAsRead(@Param("idUser") UUID idUser);
}
// funciona el chat 