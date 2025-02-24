package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.PurchaseEntity;
import com.proyect.mvp.domain.repository.PurchaseRepository;
import com.proyect.mvp.dtos.create.PurchaseCreateDTO;

import reactor.core.publisher.Mono;

public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Mono<PurchaseEntity> createPurchase(PurchaseCreateDTO purchaseDto) {
        PurchaseEntity purchase = PurchaseEntity.builder()
                                .fkUser(purchaseDto.getFkUser())
                                .level(purchaseDto.getLevel())
                                .build();

        return purchaseRepository.save(purchase);
    }
    
}
