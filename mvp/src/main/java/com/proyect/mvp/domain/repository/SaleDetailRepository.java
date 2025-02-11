package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.SaleDetailEntity;

public interface SaleDetailRepository extends R2dbcRepository<SaleDetailEntity,String> {

}
