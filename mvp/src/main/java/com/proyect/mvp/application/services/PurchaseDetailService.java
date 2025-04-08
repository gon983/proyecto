package com.proyect.mvp.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyect.mvp.application.dtos.create.PurchaseDetailCreateDTO;
import com.proyect.mvp.application.dtos.requests.ProductsPayedDTO;


import com.proyect.mvp.application.dtos.update.PurchaseDetailUpdateDTO;
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.PurchaseDetailStateEntity;


import com.proyect.mvp.domain.repository.PurchaseDetailRepository;
import com.proyect.mvp.infrastructure.exception.PurchaseDetailNotInPendingStateException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PurchaseDetailService {


    private final PurchaseDetailRepository purchaseDetailRepository;
    private final PurchaseDetailStateService purchaseDetailStateService;
    private final ProductService productService;

    public PurchaseDetailService(PurchaseDetailRepository purchaseDetailRepository, PurchaseDetailStateService purchaseDetailStateService, ProductService productService) {
        this.purchaseDetailRepository = purchaseDetailRepository;
        this.purchaseDetailStateService = purchaseDetailStateService;
        this.productService = productService;
    
    }

    public Mono<PurchaseDetailEntity> createPurchaseDetail(UUID fkBuyer, UUID fkPurchase, PurchaseDetailCreateDTO purchaseDetailDto){
        return productService.getProductById(purchaseDetailDto.getFkProduct())
                             .flatMap(product -> {
                            

                                return purchaseDetailStateService.findByName("pending")
                                  .flatMap(purchaseState ->{
        PurchaseDetailEntity purchaseDetail = PurchaseDetailEntity.builder()
                                                                .fkProduct(purchaseDetailDto.getFkProduct())
                                                                .fkPurchase(fkPurchase)
                                                                .quantity(purchaseDetailDto.getQuantity())
                                                                .unitPrice(purchaseDetailDto.getUnitPrice())
                                                                .fkState(purchaseState.getIdPurchaseDetailState())
                                                                .fkBuyer(fkBuyer)
                                                                
                                
                                                                .build();
        return purchaseDetailRepository.save(purchaseDetail)
                                .thenReturn(purchaseDetail)
                                .onErrorMap(error -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error Saving purchase detail", error));
        });        

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


    public Mono<Void> deleteDetailWhenBuying(UUID idDetail){
        return purchaseDetailRepository.findById(idDetail)
                                        .flatMap(detail ->{ 

                                            return purchaseDetailStateService.isDetailInPending(detail.getFkState())
                                                                              .flatMap(result ->
                                                                              {
                                                                                if(result){
                                                                                    return purchaseDetailRepository.deleteById(idDetail);

                                                                                }else{
                                                                                    return Mono.error(new PurchaseDetailNotInPendingStateException(idDetail));

                                                                                }
                                                                              });
                                        });
                                        

    }
    
    public Mono<Void> updatePurchaseDetail(UUID detailId, PurchaseDetailUpdateDTO updateDto) {
        return purchaseDetailRepository.updateQuantity(detailId, updateDto.getQuantity());
    }

 
 


   
        



















    
            
        
    

    
}
