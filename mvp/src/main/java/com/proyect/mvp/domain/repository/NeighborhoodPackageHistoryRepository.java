package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.NeighborhoodPackageHistoryEntity;

public interface NeighborhoodPackageHistoryRepository extends R2dbcRepository<NeighborhoodPackageHistoryEntity,String> {

}
