package com.proyect.mvp.application.services;

import java.util.List;
import java.util.UUID;
import com.proyect.mvp.infrastructure.routes.ONGRouter;
import org.springframework.stereotype.Service;

import com.proyect.mvp.application.dtos.response.CollectionPointSalesDTO;
import com.proyect.mvp.application.dtos.response.SaleSummaryDTO;
import com.proyect.mvp.domain.model.entities.ProductEntity;
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
                // Obtener todos los productos del productor y convertirlo a Flux
                .flatMap((ProductEntity product) -> {
                    // Para cada producto, obtener su locality y mantener el contexto del producto
                    return Mono.just(product.getFkLocality())
                        .flatMapMany(fkLocality -> neighborhoodService.getNeighborhoodOfLocality(fkLocality))
                        .flatMap(neighborhood -> collectionPointService.getCollectionPointByFkNeighborhood(neighborhood.getIdNeighborhood()))
                        .flatMap(collectionPoint -> {
                            return saleRepository.getSalesSummary(collectionPoint.getIdCollectionPoint())
                                .map(salesList -> {
                                    // Creamos el DTO con la lista de ventas
                                    return CollectionPointSalesDTO.builder()
                                        .collectionPoint(collectionPoint)
                                        .sales(salesList) // Asignamos la lista completa
                                        .build();
                                });
                        });
                })
                .collectList();
        }
            
            
    }
