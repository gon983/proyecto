package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.PurchaseEntity;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity,String> {

}
