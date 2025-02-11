package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity,String> {

}
