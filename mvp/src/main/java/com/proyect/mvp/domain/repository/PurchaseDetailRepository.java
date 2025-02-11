package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;

public interface PurchaseDetailRepository extends R2dbcRepository<PurchaseDetailEntity,String> {

}
