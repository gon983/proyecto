package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.NeighborhoodEntity;

public interface NeighborhoodRepository extends R2dbcRepository<NeighborhoodEntity,String> {

}
