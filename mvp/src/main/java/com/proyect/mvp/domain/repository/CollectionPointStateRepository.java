package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.CollectionPointStateEntity;

public interface CollectionPointStateRepository extends R2dbcRepository<CollectionPointStateEntity,String> {

}
