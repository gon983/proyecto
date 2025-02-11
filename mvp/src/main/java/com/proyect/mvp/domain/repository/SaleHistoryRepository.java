package com.proyect.mvp.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyect.mvp.domain.model.entities.SaleHistoryEntity;

public interface SaleHistoryRepository extends R2dbcRepository<SaleHistoryEntity,String> {

}
