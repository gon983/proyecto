package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.SaleStateEntity;

public interface SaleStateRepository extends JpaRepository<SaleStateEntity,String> {

}
