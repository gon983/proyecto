package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.proyect.mvp.domain.model.entities.ChatMessageEntity;

import reactor.core.publisher.Flux;

@Repository
public interface ChatMessageRepository extends R2dbcRepository<ChatMessageEntity, UUID> {
    Flux<ChatMessageEntity> findByUserIdOrderBySentAtAsc(UUID userId);
}
// funciona el chat 