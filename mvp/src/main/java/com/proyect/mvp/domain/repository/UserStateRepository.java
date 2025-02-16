package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.UserStateEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserStateRepository extends R2dbcRepository<UserStateEntity, UUID> {

    @Query("INSERT INTO userstate (id_user_state, name) VALUES (:idUserState, :name)")
    Mono<UserStateEntity> insertUserState(UUID idUserState, String name);
}