package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.SaleStateEntity;

public interface SaleStateRepository extends R2dbcRepository<SaleStateEntity,String> {

}
