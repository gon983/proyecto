package com.proyect.mvp.application.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.proyect.mvp.domain.model.entities.PurchaseEntity;
import com.proyect.mvp.domain.model.entities.PurchaseStateEntity;
import com.proyect.mvp.domain.repository.PurchaseRepository;
import com.proyect.mvp.dtos.create.PurchaseCreateDTO;

import reactor.core.publisher.Mono;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseStateService purchaseStateService;

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseStateService purchaseStateService) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseStateService = purchaseStateService;
    }

    public Mono<PurchaseEntity> createPurchase(PurchaseCreateDTO purchaseDto) {
        return purchaseStateService.findByName("pending") // Buscar estado en la DB
            .switchIfEmpty(Mono.error(new IllegalStateException("Pending state not found"))) // Manejo de error si no existe
            .flatMap(purchaseState -> { 
                PurchaseEntity purchase = PurchaseEntity.builder()
                    .fkUser(purchaseDto.getFkUser())
                    .level(purchaseDto.getLevel())
                    .createdAt(LocalDateTime.now())
                    .fkCurrentState(purchaseState.getIdPurchaseState()) // Asignar estado encontrado
                    .build();
                return purchaseRepository.save(purchase); // Guardar compra
            });
    }
    
    
}
