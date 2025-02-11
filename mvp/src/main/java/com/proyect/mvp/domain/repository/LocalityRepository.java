package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.LocalityEntity;

public interface LocalityRepository extends R2dbcRepository<LocalityEntity,String> {

}
