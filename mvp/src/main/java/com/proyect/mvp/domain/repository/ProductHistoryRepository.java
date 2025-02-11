package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.ProductHistoryEntity;

public interface ProductHistoryRepository extends JpaRepository<ProductHistoryEntity,String> {

}
