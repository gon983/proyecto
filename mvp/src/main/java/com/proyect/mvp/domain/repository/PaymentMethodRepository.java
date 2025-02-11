package com.proyect.mvp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyect.mvp.domain.model.entities.PaymentMethodEntity;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity,String> {

}
