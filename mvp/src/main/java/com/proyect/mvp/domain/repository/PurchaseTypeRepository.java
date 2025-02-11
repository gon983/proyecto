package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.PurchaseTypeEntity;

public interface PurchaseTypeRepository extends R2dbcRepository<PurchaseTypeEntity,String> {

}
