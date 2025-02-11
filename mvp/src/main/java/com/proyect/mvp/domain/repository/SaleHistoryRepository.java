package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.SaleHistoryEntity;

public interface SaleHistoryRepository extends JpaRepository<SaleHistoryEntity,String> {

}
