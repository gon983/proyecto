package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.UserEntity;

public interface UserRepository extends R2dbcRepository<UserEntity,String> {

}
