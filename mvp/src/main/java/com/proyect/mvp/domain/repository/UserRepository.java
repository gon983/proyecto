

package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.UserEntity;
import com.proyect.mvp.infrastructure.security.UserDTO;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;
import java.util.UUID;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, UUID> {
    @Query("INSERT INTO users " +
    "(id_user, username, email, created_at, first_name, last_name, " +
    "document_type, document_number, fk_neighborhood, phone, fk_role_one) " +
    "VALUES " +
    "(:#{#user.idUser}, :#{#user.username}, :#{#user.email}, " +
    ":#{#user.createdAt}, :#{#user.firstName}, :#{#user.lastName}, " +
    ":#{#user.documentType}, :#{#user.documentNumber}, :#{#user.fkNeighborhood}, " +
    ":#{#user.phone}, :#{#user.roleOne})")
    Mono<UserEntity> insertUser(@Param("user") UserEntity user);

    @Query("UPDATE users SET access_token_productor = :access_token_productor, user_productor_mp_id = :user_productor_mp_id, refresh_productor_token = :refresh_productor_token WHERE id_user = :productor_id")
    Mono<Void> saveProducerMpData(
    @Param("productor_id") UUID productorId,
    @Param("access_token_productor") String accessTokenProductor,
    @Param("user_productor_mp_id") String userProductorMpId,
    @Param("refresh_productor_token") String refreshProductorToken
);

    @Query("UPDATE users SET access_token_productor = :newAccessToken, refresh_productor_token = :newRefreshToken WHERE id_user = :productorId  ")
    Mono<Void> updateProducerMpTokens(
        @Param("productorId") UUID productorId, 
        @Param("newAccessToken")String newAccessToken, 
        @Param("newRefreshToken") String newRefreshToken);

        @Query("SELECT (u.id_user, u.username, u.email, u.password, u.role) from  users u where username = :username")
        Mono<UserDTO> findByUsername(@Param("username") String username);
}