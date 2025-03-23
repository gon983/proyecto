package com.proyect.mvp.domain.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.proyect.mvp.domain.model.entities.PaymentMethodEntity;

public interface PaymentMethodRepository extends R2dbcRepository<PaymentMethodEntity,String> {

}
