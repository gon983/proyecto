package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.UserHistoryEntity;
import com.proyect.mvp.domain.repository.UserHistoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserHistoryService {

    private final UserHistoryRepository userHistoryRepository;

    public UserHistoryService(UserHistoryRepository userHistoryRepository) {
        this.userHistoryRepository = userHistoryRepository;
    }

    public Flux<UserHistoryEntity> getUserHistory(UUID userId) {
        return userHistoryRepository.findByUserId(userId);
    }

    public Mono<UserHistoryEntity> registerUserStateChange(UUID userId, UUID newUserStateId, LocalDateTime initialDate, LocalDateTime finalDate, String description) {
        UUID idUserHistory = UUID.randomUUID();

        return userHistoryRepository.insertUserHistory(idUserHistory, userId, newUserStateId, initialDate, finalDate, description);
    }
}