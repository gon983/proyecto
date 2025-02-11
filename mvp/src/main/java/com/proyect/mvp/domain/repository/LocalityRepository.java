package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.LocalityEntity;

public interface LocalityRepository extends R2dbcRepository<LocalityEntity,String> {

}
