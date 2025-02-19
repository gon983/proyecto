

package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface UserRepository extends R2dbcRepository<UserEntity, UUID> {
    @Query("INSERT INTO user (id_user, name, email) VALUES (:id, :name, :email)")
    Mono<UserEntity> insertUser(@Param("id") UUID id, @Param("name") String name, @Param("email") String email);
}