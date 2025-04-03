

package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.UserEntity;
import com.proyect.mvp.infrastructure.security.UserAuthenticationDTO;


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

    

    

    @Query("SELECT * from  users u where username = :username")
    Mono<UserAuthenticationDTO> findByUsername(@Param("username") String username);


    @Query("UPDATE users SET password = :password WHERE email = :email")
    Mono<Void> updatePassword(@Param("email") String email, @Param("password") String password);


}