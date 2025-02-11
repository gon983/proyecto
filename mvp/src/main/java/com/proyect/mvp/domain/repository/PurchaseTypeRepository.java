package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.PurchaseTypeEntity;

public interface PurchaseTypeRepository extends JpaRepository<PurchaseTypeEntity,String> {

}
