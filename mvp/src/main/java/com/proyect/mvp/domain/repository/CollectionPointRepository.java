package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.CollectionPointEntity;

public interface CollectionPointRepository extends R2dbcRepository<CollectionPointEntity,String> {

}
