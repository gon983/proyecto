package com.proyect.mvp.domain.repository;


import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import org.springframework.stereotype.Repository;

import com.proyect.mvp.domain.model.entities.RecorridoEntity;

@Repository
public interface RecorridoRepository extends R2dbcRepository<RecorridoEntity, UUID> {
}
