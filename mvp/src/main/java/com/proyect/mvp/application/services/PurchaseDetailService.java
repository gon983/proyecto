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

import com.proyect.mvp.application.dtos.response.JustPayedSalesDto;
import com.proyect.mvp.application.dtos.response.SaleSummaryDTO;

import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.PurchaseDetailStateEntity;


import com.proyect.mvp.domain.repository.PurchaseDetailRepository;

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
                                                                .fkProductor(purchaseDetailDto.getFkProductor())
                                                                .fkProductor(product.getFkProductor())
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

 
 


   
        











// public Mono<List<SaleSummaryDTO>> getSalesSummary(UUID idCollectionPoint) {
//     return purchaseDetailStateService.findByName("confirmed")
//                             .flatMap(stateA -> {
//                                 return purchaseDetailStateService.findByName("payed")
//                                 .flatMap(stateB -> {
//                                     return purchaseDetailRepository.getSalesConfirmedOrPayedForCollectionPoint(idCollectionPoint, stateA.getIdPurchaseDetailState(), stateB.getIdPurchaseDetailState())
//                                             .flatMap(sale -> productService.getProductById(sale.getFkProduct())
//                                                 .map(product -> new SaleSummaryDTO(
//                                                     product.getIdProduct(),
//                                                     product.getName(),
//                                                     product.getStock(),
//                                                     product.getUnitMeasurement(),
//                                                     sale.getQuantity(),
//                                                     sale.calculatePrice()
//                                                 ))
//                                             )
//                                             .collect(Collectors.toMap(
//                                                 SaleSummaryDTO::getIdProduct,  // 🔹 Agrupamos por idProduct
//                                                 saleSummary -> saleSummary,    // 🔹 Mantenemos el primer objeto tal cual
//                                                 (existing, newSale) -> {       // 🔥 Merge: Sumamos cantidades y montos sin perder datos
//                                                     existing.setTotalQuantity(existing.getTotalQuantity() + newSale.getTotalQuantity());
//                                                     existing.setTotalAmount(existing.getTotalAmount() + newSale.getTotalAmount());
//                                                     return existing;
//                                                 }
//                                             ))
//                                             .map(groupedMap -> new ArrayList<>(groupedMap.values())); // Convertimos Map a List
//                                                                 });
//                             });
                            
    
    
    
}




public Mono<CollectionPointSalesDTO> obtenerTodasLasVentasConfirmadasOPagadasDeUnCpSumarizadasPorProduct(UUID idCollectionPoint){
    return getSalesSummary(idCollectionPoint)
                          .map(sales -> {
                            CollectionPointSalesDTO dto = CollectionPointSalesDTO.builder() 
                                                                                 .sales(sales)
                                                                                 .build();
                            return dto;
                          });

}


    
            
        
    

    
}
