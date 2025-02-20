

package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface UserRepository extends R2dbcRepository<UserEntity, UUID> {
    @Query("INSERT INTO user " +
    "(id_user, username, email, created_at, first_name, last_name, " +
    "document_type, document_number, fk_neighborhood, phone, fk_role_one) " +
    "VALUES " +
    "(:#{#user.idUser}, :#{#user.username}, :#{#user.email}, " +
    ":#{#user.createdAt}, :#{#user.firstName}, :#{#user.lastName}, " +
    ":#{#user.documentType}, :#{#user.documentNumber}, :#{#user.fkNeighborhood}, " +
    ":#{#user.phone}, :#{#user.roleOne})")
    Mono<UserEntity> insertUser(@Param("user") UserEntity user);
}