package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.BigSaleEntity;

public interface BigSaleRepository extends R2dbcRepository<BigSaleEntity,String> {

}
