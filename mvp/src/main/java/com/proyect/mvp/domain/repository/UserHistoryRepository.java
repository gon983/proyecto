package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.UserHistoryEntity;

public interface UserHistoryRepository extends R2dbcRepository<UserHistoryEntity,String> {

}
