package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.NeighborhoodPackageEntity;

public interface NeighborhoodPackageRepository extends R2dbcRepository<NeighborhoodPackageEntity,String> {

}
