package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.StandarProductEntity;

public interface StandarProductRepository extends R2dbcRepository<StandarProductEntity, UUID> {
    
}
