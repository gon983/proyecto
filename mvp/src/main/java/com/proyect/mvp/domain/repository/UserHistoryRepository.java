package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.UserHistoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface UserHistoryRepository extends R2dbcRepository<UserHistoryEntity, UUID> {

    @Query("SELECT * FROM user_history WHERE id_user = :userId ORDER BY initial_date ASC")
    Flux<UserHistoryEntity> findByUserId(UUID userId);

    @Query("INSERT INTO user_history (id_user_history, id_user, id_user_state, initial_date, final_date, description) " +
            "VALUES (:idUserHistory, :idUser, :idUserState, :initialDate, :finalDate, :description)")
    Mono<UserHistoryEntity> insertUserHistory(UUID idUserHistory, UUID idUser, UUID idUserState, LocalDateTime initialDate, LocalDateTime finalDate, String description);
}
