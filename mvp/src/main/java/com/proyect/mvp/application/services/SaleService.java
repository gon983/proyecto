package com.proyect.mvp.application.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.proyect.mvp.infrastructure.routes.ONGRouter;
import org.springframework.stereotype.Service;

import com.proyect.mvp.application.dtos.requests.ProductsPayedDTO;
import com.proyect.mvp.application.dtos.response.CollectionPointSalesDTO;
import com.proyect.mvp.application.dtos.response.JustPayedSalesDto;
import com.proyect.mvp.application.dtos.response.SaleSummaryDTO;
import com.proyect.mvp.domain.model.entities.BigSaleEntity;
import com.proyect.mvp.domain.model.entities.ProductEntity;
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.SaleEntity;
import com.proyect.mvp.domain.repository.ONGRepository;
import com.proyect.mvp.domain.repository.SaleRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SaleService {

    private final ONGRouter ONGRouter;

    private final ONGRepository ONGRepository;
    private final SaleRepository saleRepository;
    private final PurchaseDetailService detailService;
    private final SaleStateService saleStateService;
    private final UserService userService;
    private final ProductService productService;
    private final NeighborhoodService neighborhoodService;
    private final CollectionPointService collectionPointService;

    public SaleService(SaleRepository saleRepository, ONGRepository ONGRepository, PurchaseDetailService purchaseDetailService, 
    ONGRouter ONGRouter, SaleStateService saleStateService, UserService userService, ProductService productService, NeighborhoodService neighborhoodService,
    CollectionPointService collectionPointService){
        this.saleRepository = saleRepository;
        this.ONGRepository = ONGRepository;
        this.detailService = purchaseDetailService;
        this.ONGRouter = ONGRouter;
        this.saleStateService = saleStateService;
        this.userService = userService;
        this.productService = productService;
        this.neighborhoodService = neighborhoodService;
        this.collectionPointService = collectionPointService;
    }
    

    public Mono<SaleEntity> registrarVenta(String fkDetail, UUID fkProductor, UUID fkUser){
        return userService.getUserById(fkUser)
            .flatMap(user -> {return saleStateService.findSaleStateByName("pending_payment")
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
                                                                        .fkBuyer(fkUser)
                                                                        .fkCollectionPoint(user.getFkCollectionPointSuscribed())
                                                                        .build();
                                                        return saleRepository.save(sale);
                                                    })
                                            );});
        
        }






public Mono<List<CollectionPointSalesDTO>> obtenerVentasProductorPorCollectionPoint(UUID idProductor) {
    return productService.getCollectionsPointsThatCouldSellTheProduct(idProductor)
        
                // Para cada punto, obtenemos las ventas y creamos el DTO
                .flatMap(collectionPoint -> {
                    return this.getSalesSummary(collectionPoint.getIdCollectionPoint(), idProductor)
                        .map(salesList -> {
                            return CollectionPointSalesDTO.builder()
                                    .collectionPoint(collectionPoint)
                                    .sales(salesList)
                                    .build();
                        });
                })
                // Combinamos todos los resultados en una lista
                .collectList();
        
}

public Mono<CollectionPointSalesDTO> obtenerVentasProductorDeCollectionPoint(UUID idProductor, UUID idCollectionPoint) {
    return getSalesSummary(idCollectionPoint, idProductor)
                            .map(salesList -> {
                                return CollectionPointSalesDTO.builder()
                                        .sales(salesList)
                                        .build();
                            });
            
}

public Flux<JustPayedSalesDto> registrarPagoVentasCollectionPointDeProductor(UUID idProductor, UUID idCollectionPoint, ProductsPayedDTO listPayedProducts) {
    return saleStateService.findSaleStateByName("payed")
        .flatMapMany(state -> {
            return Flux.fromIterable(listPayedProducts.getProductsPayed())
                .flatMap(idProduct -> {return registerSalesAsPayed(idProductor, idCollectionPoint, idProduct, state.getIdSaleState())
                                                    .collectList()
                                                    .map(savedSales -> {
                                                        JustPayedSalesDto salesDTO = JustPayedSalesDto.builder()
                                                                                                        .idProduct(idProduct)
                                                                                                        .sales(savedSales)
                                                                                                        .build();
    
                        // Configurar DTO
                                                    return salesDTO;
                                                });
                });
                
            });
}

public Flux<SaleEntity> registerSalesAsPayed(UUID idProductor,UUID idCollectionPoint, UUID idProduct, UUID idSaleState){
    return saleStateService.findSaleStateByName("pending_payment")
                            .flatMapMany(state ->{
                                    return saleRepository.getSalesPendingPaymentForProductAndCollectionPointAndProducer(idProductor, idCollectionPoint,state.getIdSaleState(),idProduct)
                                                                        .flatMap(sale -> {
                                                                            sale.setCurrentState(idSaleState);
                                                                            return saleRepository.save(sale);
                                                                        });
                                                                    });
}


public Mono<List<SaleSummaryDTO>> getSalesSummary(UUID idCollectionPoint, UUID idProducer) {
    return saleStateService.findSaleStateByName("pending_payment")
                            .flatMap(state -> {
                                return saleRepository.getSalesPendingPaymentForCollectionPointAndProducer(idCollectionPoint, idProducer, state.getIdSaleState())
                                        .flatMap(sale -> productService.getProductById(sale.getFkProduct())
                                            .map(product -> new SaleSummaryDTO(
                                                product.getIdProduct(),
                                                product.getName(),
                                                product.getStock(),
                                                product.getUnitMeasurement(),
                                                sale.getQuantity(),
                                                sale.getAmount()
                                            ))
                                        )
                                        .collect(Collectors.toMap(
                                            SaleSummaryDTO::getIdProduct,  // 🔹 Agrupamos por idProduct
                                            saleSummary -> saleSummary,    // 🔹 Mantenemos el primer objeto tal cual
                                            (existing, newSale) -> {       // 🔥 Merge: Sumamos cantidades y montos sin perder datos
                                                existing.setTotalQuantity(existing.getTotalQuantity() + newSale.getTotalQuantity());
                                                existing.setTotalAmount(existing.getTotalAmount() + newSale.getTotalAmount());
                                                return existing;
                                            }
                                        ))
                                        .map(groupedMap -> new ArrayList<>(groupedMap.values())); // Convertimos Map a List
                                                            });
    
    
    
}


        
            
            
    }
