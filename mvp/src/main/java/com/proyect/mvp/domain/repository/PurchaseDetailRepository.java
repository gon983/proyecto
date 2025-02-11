package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;

public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetailEntity,String> {

}
