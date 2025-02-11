package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.NeighborhoodEntity;

public interface NeighborhoodRepository extends R2dbcRepository<NeighborhoodEntity,String> {

}
