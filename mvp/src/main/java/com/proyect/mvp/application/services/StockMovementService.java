package com.proyect.mvp.application.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import com.proyect.mvp.application.dtos.create.StockMovementCreateDTO;
import com.proyect.mvp.domain.model.entities.StockMovementEntity;
import com.proyect.mvp.domain.model.entities.StockMovementTypeEnum;
import com.proyect.mvp.domain.repository.StockMovementRepository;

import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Mono;


@Service
public class StockMovementService {
    private final StockMovementRepository stockMovementRepository;
    private final ProductService productService;
    private final TransactionalOperator transactionalOperator;
    private final PurchaseDetailService detailService;

    public StockMovementService(StockMovementRepository stockMovementRepository, ProductService productService,
     ConnectionFactory connectionFactory, PurchaseDetailService detailService){
        this.stockMovementRepository = stockMovementRepository;
        this.productService = productService;
        this.transactionalOperator = TransactionalOperator.create(new R2dbcTransactionManager(connectionFactory));
        this.detailService = detailService;

    }

    public Mono<Void> registerMovement(UUID userId, StockMovementCreateDTO dto){
        return productService.getProductById(dto.getFkProduct())
                            .flatMap(
                                product -> {
                                    double newStock = product.getStock() +dto.getQuantity();
                                    if(newStock<0){return Mono.error(new IllegalArgumentException("Stock Insuficiente"));}

                                    StockMovementEntity movement =
                                    StockMovementEntity.builder()
                                                        .fkProduct(dto.getFkProduct())
                                                        .fkUser(userId)
                                                        .type(dto.getType())
                                                        .quantity(dto.getQuantity())
                                                        .date(LocalDateTime.now())
                                                        .comment(dto.getComment())
                                                        .build();
                                    
                                    return productService.updateStock(product.getIdProduct(), newStock)
                                                        .then(stockMovementRepository.save(movement))
                                                        .then();

                                })
                                .as(transactionalOperator::transactional)
                                .then();
                                


    }

    public Mono<Void> registrarMovimientoPorCompra(UUID fkUser, String idDetail){
        return detailService.getById(UUID.fromString(idDetail))
                            .flatMap(detail -> {
                            return productService.getProductById(detail.getFkProduct())
                            .flatMap(
                                product -> {
                                    double newStock = product.getStock() +detail.getQuantity();
                                    if(newStock<0){return Mono.error(new IllegalArgumentException("Stock Insuficiente"));}

                                    StockMovementEntity movement =
                                    StockMovementEntity.builder()
                                                        .fkProduct(detail.getFkProduct())
                                                        .fkUser(fkUser)
                                                        .type(StockMovementTypeEnum.DECREASE)
                                                        .quantity(detail.getQuantity())
                                                        .date(LocalDateTime.now())
                                                        .comment("")
                                                        .build();
                                    
                                    return productService.updateStock(product.getIdProduct(), newStock)
                                                        .then(stockMovementRepository.save(movement))
                                                        .then();

                                }); });
    }


    
}
