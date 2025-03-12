package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.VoteEntity;

public interface VoteRepository extends R2dbcRepository<VoteEntity, UUID> {
    
}
