package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.UserEntity;

public interface UserRepository extends R2dbcRepository<UserEntity,String> {

}
