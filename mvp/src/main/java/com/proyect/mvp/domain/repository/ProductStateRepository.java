package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.ProductStateEntity;

public interface ProductStateRepository extends JpaRepository<ProductStateEntity,String> {

}
