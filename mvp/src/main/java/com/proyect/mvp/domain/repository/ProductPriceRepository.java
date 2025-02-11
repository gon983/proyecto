package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.ProductPriceEntity;

public interface ProductPriceRepository extends JpaRepository<ProductPriceEntity,String> {

}
