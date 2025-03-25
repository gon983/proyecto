package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.CollectionPointPaymentsEntity;

public interface CollectionPointPaymentsRepository extends R2dbcRepository<CollectionPointPaymentsEntity,String> {

}
