package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.NeighborhoodPackageStateEntity;

public interface NeighborhoodPackageStateRepository extends R2dbcRepository<NeighborhoodPackageStateEntity,String> {

}
