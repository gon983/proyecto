package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.SaleEntity;

public interface SaleRepository extends R2dbcRepository<SaleEntity,String> {

}
