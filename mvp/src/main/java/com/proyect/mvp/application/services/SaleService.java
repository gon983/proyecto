package com.proyect.mvp.application.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.proyect.mvp.infrastructure.routes.ONGRouter;
import org.springframework.stereotype.Service;

import com.proyect.mvp.application.dtos.response.CollectionPointSalesDTO;
import com.proyect.mvp.application.dtos.response.SaleSummaryDTO;
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
        return productService.getProductsByProducer(idProductor)
            .collectList()
            // Tomamos solo el primer producto (ya que todos tienen la misma localidad)
            .flatMap(productList -> {
                
                // Obtenemos la localidad del primer producto
                UUID locality = productList.get(0).getFkLocality();
                System.out.println("Procesando localidad: " + locality);
                
                // Obtenemos todos los barrios de esta localidad
                return neighborhoodService.getNeighborhoodsOfLocality(locality)
                    .doOnNext(neighborhoods -> {
                        System.out.println("Barrios encontrados: " + neighborhoods.size());
                        neighborhoods.forEach(n -> System.out.println("  - Barrio: " + n.getIdNeighborhood()));
                    })
                    // Convertimos la lista de barrios a un Flux para procesarlos
                    .flatMapMany(neighborhoods -> Flux.fromIterable(neighborhoods))
                    // Para cada barrio, obtenemos los puntos de recolección
                    .flatMap(neighborhood -> {
                        System.out.println("Procesando barrio: " + neighborhood.getIdNeighborhood());
                        return collectionPointService.getCollectionPointByFkNeighborhood(neighborhood.getIdNeighborhood())
                            .doOnNext(cp -> System.out.println("  - Punto encontrado: " + cp.getIdCollectionPoint()));
                    })
                    // Para cada punto, obtenemos las ventas y creamos el DTO
                    .flatMap(collectionPoint -> {
                        return this.getSalesSummary(collectionPoint.getIdCollectionPoint())
                            .map(salesList -> {
                                System.out.println("  - Ventas para punto " + collectionPoint.getIdCollectionPoint() + ": " + salesList.size());
                                return CollectionPointSalesDTO.builder()
                                        .collectionPoint(collectionPoint)
                                        .sales(salesList)
                                        .build();
                            });
                    })
                    // Combinamos todos los resultados en una lista
                    .collectList();
            });
}

    public Mono<List<SaleSummaryDTO>> getSalesSummary(UUID idCollectionPoint) {
    return saleRepository.getSalesForCollectionPoint(idCollectionPoint)
        .flatMap(sale -> 
            productService.getProductById(sale.getFkProduct())  // Obtener producto
                .map(product -> new SaleSummaryDTO(
                    product.getIdProduct(),
                    product.getName(),
                    product.getStock(),
                    product.getUnitMeasurement(),
                    sale.getQuantity(),
                    sale.getAmount()
                ))
        )
        .collect(Collectors.groupingBy(
            SaleSummaryDTO::getIdProduct,   // Agrupar por idProduct
            Collectors.reducing(
                new SaleSummaryDTO(null, "", 0.0, "", 0.0, 0.0), // Valor inicial
                (sale1, sale2) -> new SaleSummaryDTO(
                    sale1.getIdProduct(),
                    sale1.getName(),
                    sale1.getStock(),
                    sale1.getUnitMeasurement(),
                    sale1.getTotalQuantity() + sale2.getTotalQuantity(), // Sumar cantidades
                    sale1.getTotalAmount() + sale2.getTotalAmount()      // Sumar montos
                )
            )
        ))
        .map(groupedMap -> new ArrayList<>(groupedMap.values())); // Convertir Map a List
}


        
            
            
    }
