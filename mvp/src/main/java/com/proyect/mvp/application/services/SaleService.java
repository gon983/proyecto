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

    public SaleService(SaleRepository saleRepository, ONGRepository ONGRepository, PurchaseDetailService purchaseDetailService, ONGRouter ONGRouter){
        this.saleRepository = saleRepository;
        this.ONGRepository = ONGRepository;
        this.detailService = purchaseDetailService;
        this.ONGRouter = ONGRouter;
    }
    

    public Mono<SaleEntity> registrarVenta(String fkDetail, UUID fkProductor){
        return  detailService.getById(UUID.fromString(fkDetail))
                            .flatMap(detail -> {

                                SaleEntity sale = SaleEntity.builder()
                                                            .amount(detail.calculatePrice())
                                                            .fkProductor(fkProductor)
                                                            .fkProduct(detail.getProduct().getIdProduct())
                                                            .quantity(detail.getQuantity())
                                                            .unitPrice(detail.getUnitPrice())
                                                            .build();
                                return saleRepository.save(sale);

                            });

    }
}
