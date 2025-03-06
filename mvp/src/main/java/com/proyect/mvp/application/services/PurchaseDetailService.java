package com.proyect.mvp.application.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyect.mvp.domain.model.dtos.create.PurchaseDetailCreateDTO;
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.PurchaseDetailStateEntity;
import com.proyect.mvp.domain.repository.PurchaseDetailRepository;

import reactor.core.publisher.Flux;
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
        return purchaseDetailStateService.findByName("pending")
                                  .flatMap(purchaseState ->{
        PurchaseDetailEntity purchaseDetail = PurchaseDetailEntity.builder()
                                                                .fkProduct(purchaseDetailDto.getFkProduct())
                                                                .fkPurchase(fkPurchase)
                                                                .quantity(purchaseDetailDto.getQuantity())
                                                                .unitPrice(purchaseDetailDto.getUnitPrice())
                                                                .fkState(purchaseState.getIdPurchaseDetailState())
                                                                .build();
        return purchaseDetailRepository.save(purchaseDetail)
                                .thenReturn(purchaseDetail)
                                .onErrorMap(error -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error Saving purchase detail", error));
        });        
    }

    public Flux<PurchaseDetailEntity> getDetailsFromPurchase(UUID idPurchase){
        return purchaseDetailRepository.findAllByFkPurchase(idPurchase);

    }

    
}
