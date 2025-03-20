package com.proyect.mvp.application.services;

import java.util.UUID;
import com.proyect.mvp.infrastructure.routes.ONGRouter;
import org.springframework.stereotype.Service;

import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.SaleEntity;
import com.proyect.mvp.domain.repository.ONGRepository;
import com.proyect.mvp.domain.repository.SaleRepository;

import reactor.core.publisher.Mono;

@Service
public class SaleService {

    private final ONGRouter ONGRouter;

    private final ONGRepository ONGRepository;
    private final SaleRepository saleRepository;
    private final PurchaseDetailService detailService;
    private final SaleStateService saleStateService;

    public SaleService(SaleRepository saleRepository, ONGRepository ONGRepository, PurchaseDetailService purchaseDetailService, ONGRouter ONGRouter, SaleStateService saleStateService){
        this.saleRepository = saleRepository;
        this.ONGRepository = ONGRepository;
        this.detailService = purchaseDetailService;
        this.ONGRouter = ONGRouter;
        this.saleStateService = saleStateService;
    }
    

    public Mono<SaleEntity> registrarVenta(String fkDetail, UUID fkProductor){
        return saleStateService.findSaleStateByName("pending_payment")
                .flatMap(state -> 
                            detailService.getById(UUID.fromString(fkDetail))
                                .flatMap(detail -> {
                                                    SaleEntity sale = SaleEntity.builder()
                                                                        .amount(detail.calculatePrice())
                                                                        .fkCurrentState(state.getIdSaleState())
                                                                        .fkProductor(fkProductor)
                                                                        .fkProduct(detail.getProduct().getIdProduct())
                                                                        .quantity(detail.getQuantity())
                                                                        .unitPrice(detail.getUnitPrice())
                                                                        .build();
                                                        return saleRepository.save(sale);
                                                    })
                                            );
                }}
