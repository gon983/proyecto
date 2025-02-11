package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.ProductCategoryEntity;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity,String> {

}
