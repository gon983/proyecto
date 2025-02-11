package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.UserHistoryEntity;

public interface UserHistoryRepository extends R2dbcRepository<UserHistoryEntity,String> {

}
