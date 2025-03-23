package com.proyect.mvp.application.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyect.mvp.application.dtos.create.PurchaseDetailCreateDTO;
import com.proyect.mvp.domain.model.entities.DefaultProductxCollectionPointxWeekEntity;
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.PurchaseDetailStateEntity;
import com.proyect.mvp.domain.repository.ONGRepository;
import com.proyect.mvp.domain.repository.PurchaseDetailRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PurchaseDetailService {

    private final ONGRepository ONGRepository;
    private final PurchaseDetailRepository purchaseDetailRepository;
    private final PurchaseDetailStateService purchaseDetailStateService;
    private final ProductService productService;

    public PurchaseDetailService(PurchaseDetailRepository purchaseDetailRepository, PurchaseDetailStateService purchaseDetailStateService, ProductService productService, ONGRepository ONGRepository) {
        this.purchaseDetailRepository = purchaseDetailRepository;
        this.purchaseDetailStateService = purchaseDetailStateService;
        this.productService = productService;
        this.ONGRepository = ONGRepository;
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

    public Flux<PurchaseDetailEntity> getDetailsFromPurchaseWithProducts(UUID idPurchase){
        return purchaseDetailRepository.findAllByFkPurchase(idPurchase)
                                        .flatMap(detail -> productService.getProductById(detail.getFkProduct())
                                                            .map(product -> {detail.addProduct(product);
                                                                            return detail;}));   
    }

    public Mono<PurchaseDetailEntity> getById(UUID idDetail){
        return purchaseDetailRepository.findByIdPurchaseDetail(idDetail)
        .flatMap(detail -> productService.getProductById(detail.getFkProduct())
                                                            .map(product -> {detail.addProduct(product);
                                                                            return detail;}));
    }

    public Mono<PurchaseDetailEntity> save(PurchaseDetailEntity detail) {
        return purchaseDetailRepository.save(detail);
    }

    public Mono<List<DefaultProductxCollectionPointxWeekEntity>> filterDpCpByPurchase(UUID idPurchase, List<DefaultProductxCollectionPointxWeekEntity> dpcpList) {
    return this.getDetailsFromPurchase(idPurchase)
            .collectList()
            .map(listdetails -> {
                // Extraer todos los IDs de productos de los detalles para comparación más rápida
                Set<UUID> productIds = listdetails.stream()
                    .map(PurchaseDetailEntity::getFkProduct)
                    .collect(Collectors.toSet());
                
                // Filtrar la lista original basado en si el producto está en el conjunto
                return dpcpList.stream()
                    .filter(dpcp -> productIds.contains(dpcp.getFkProduct()))
                    .collect(Collectors.toList());
            });
}

    
            
        
    

    
}
