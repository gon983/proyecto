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
import com.proyect.mvp.application.dtos.response.CollectionPointSalesDTO;
import com.proyect.mvp.application.dtos.response.JustPayedSalesDto;
import com.proyect.mvp.application.dtos.response.SaleSummaryDTO;
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

    public Mono<PurchaseDetailEntity> createPurchaseDetail(UUID fkBuyer, UUID fkCollectionPoint, UUID fkPurchase, PurchaseDetailCreateDTO purchaseDetailDto){
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
                                                                .fkCollectionPoint(fkCollectionPoint)
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

    public Mono<List<CollectionPointSalesDTO>> obtenerVentasProductorPorCollectionPoint(UUID idProductor) {
    return productService.getCollectionsPointsThatCouldSellTheProduct(idProductor)
        
                // Para cada punto, obtenemos las ventas y creamos el DTO
                .flatMap(collectionPoint -> {
                    return getSalesSummary(collectionPoint.getIdCollectionPoint(), idProductor)
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
    System.out.println("START: registrarPagoVentasCollectionPointDeProductor");
    System.out.println("Input - Producer ID: " + idProductor);
    System.out.println("Input - Collection Point ID: " + idCollectionPoint);
    System.out.println("Input - Products to Pay: " + (listPayedProducts != null ? listPayedProducts.getProductsPayed() : "NULL"));

    return purchaseDetailStateService.findByName("payed")
        .doOnNext(statel -> System.out.println("Found Sale State - ID: " + statel.getIdPurchaseDetailState()))
        .doOnError(error -> System.err.println("Error finding sale state: " + error.getMessage()))
        .flatMapMany(state -> {
            System.out.println("Processing products for sale state: " + state.getIdPurchaseDetailState());
            
            return Flux.fromIterable(listPayedProducts.getProductsPayed())
                .flatMap(idProduct -> {
                    System.out.println("Processing product: " + idProduct);
                    
                    return registerSalesAsPayed(idProductor, idCollectionPoint, idProduct, state.getIdPurchaseDetailState())
                        .collectList()
                        .doOnError(error -> System.err.println("Error registering sales for product " + idProduct + ": " + error.getMessage()))
                        .map(savedSales -> {
                            System.out.println("Saved sales for product " + idProduct + ": " + savedSales.size() + " sales");
                            
                            JustPayedSalesDto salesDTO = JustPayedSalesDto.builder()
                                .idProduct(idProduct)
                                .sales(savedSales)
                                .build();
                            
                            System.out.println("Created JustPayedSalesDto for product: " + idProduct);
                            return salesDTO;
                        });
                })
                .doOnError(error -> System.err.println("Error in product processing: " + error.getMessage()));
        })
        .doOnError(error -> System.err.println("Overall method error: " + error.getMessage()))
        .doOnComplete(() -> System.out.println("Method completed successfully"));
}

public Flux<PurchaseDetailEntity> registerSalesAsPayed(UUID idProductor,UUID idCollectionPoint, UUID idProduct, UUID idSaleState){
    return purchaseDetailStateService.findByName("confirmed")
                            .flatMapMany(state ->{
                                    return purchaseDetailRepository.getSalesConfirmedForProductAndCollectionPointAndProducer( idCollectionPoint, idProductor,state.getIdPurchaseDetailState(),idProduct)
                                                                        .flatMap(sale -> {
                                                                            sale.setFkCurrentState(idSaleState);
                                                                            return purchaseDetailRepository.save(sale);
                                                                        });
                                                                    });
}


public Mono<List<SaleSummaryDTO>> getSalesSummary(UUID idCollectionPoint, UUID idProducer) {
    return purchaseDetailStateService.findByName("confirmed")
                            .flatMap(state -> {
                                return purchaseDetailRepository.getSalesConfirmedForCollectionPointAndProducer(idCollectionPoint, idProducer, state.getIdPurchaseDetailState())
                                        .flatMap(sale -> productService.getProductById(sale.getFkProduct())
                                            .map(product -> new SaleSummaryDTO(
                                                product.getIdProduct(),
                                                product.getName(),
                                                product.getStock(),
                                                product.getUnitMeasurement(),
                                                sale.getQuantity(),
                                                sale.calculatePrice()
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
