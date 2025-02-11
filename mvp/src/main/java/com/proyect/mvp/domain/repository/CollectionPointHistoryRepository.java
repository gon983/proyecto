package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.CollectionPointHistoryEntity;



public interface CollectionPointHistoryRepository extends R2dbcRepository<CollectionPointHistoryEntity,String> {

}
