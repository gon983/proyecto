package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.NeighborhoodPackageHistoryEntity;

public interface NeighborhoodPackageHistoryRepository extends R2dbcRepository<NeighborhoodPackageHistoryEntity,String> {

}
