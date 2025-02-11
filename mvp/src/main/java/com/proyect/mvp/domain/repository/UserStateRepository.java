package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.UserStateEntity;

public interface UserStateRepository extends R2dbcRepository<UserStateEntity,String> {

}
