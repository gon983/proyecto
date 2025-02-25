package com.proyect.mvp.application.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.repository.PurchaseDetailRepository;
import com.proyect.mvp.dtos.create.PurchaseDetailCreateDTO;

import reactor.core.publisher.Mono;
@Service
public class PurchaseDetailService {
    private final PurchaseDetailRepository purchaseDetailRepository;
    private final PurchaseDetailStateService purchaseDetailStateService;

    public PurchaseDetailService(PurchaseDetailRepository purchaseDetailRepository, PurchaseDetailStateService purchaseDetailStateService) {
        this.purchaseDetailRepository = purchaseDetailRepository;
        this.purchaseDetailStateService = purchaseDetailStateService;
    }

    public Mono<PurchaseDetailEntity> createPurchaseDetail(UUID fkPurchase, PurchaseDetailCreateDTO purchaseDetailDto){
        
        
        PurchaseDetailEntity purchaseDetail = PurchaseDetailEntity.builder()
                                                                .fkProduct(purchaseDetailDto.getFkProduct())
                                                                .fkPurchase(fkPurchase)
                                                                .quantity(purchaseDetailDto.getQuantity())
                                                                .unitPrice(purchaseDetailDto.getUnitPrice())
                                                                .build();
        return purchaseDetailRepository.save(purchaseDetail)
                                .thenReturn(purchaseDetail)
                                .onErrorMap(error -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error Saving purchase detail", error));        
    }

    
}
