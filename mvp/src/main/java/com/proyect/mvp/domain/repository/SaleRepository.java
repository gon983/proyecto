package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.SaleEntity;

public interface SaleRepository extends JpaRepository<SaleEntity,String> {

}
