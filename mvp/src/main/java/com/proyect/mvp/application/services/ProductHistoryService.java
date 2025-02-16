package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.ProductHistoryEntity;
import com.proyect.mvp.domain.repository.ProductHistoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductHistoryService {

    private final ProductHistoryRepository productHistoryRepository;

    public ProductHistoryService(ProductHistoryRepository productHistoryRepository) {
        this.productHistoryRepository = productHistoryRepository;
    }

    public Flux<ProductHistoryEntity> getProductHistory(UUID productId) {
        return productHistoryRepository.findByProductId(productId);
    }

    public Mono<ProductHistoryEntity> registerProductStateChange(UUID productId, UUID newProductStateId, UUID previousProductStateId, UUID changedBy) {
        UUID idProductHistory = UUID.randomUUID();
        LocalDateTime changeDate = LocalDateTime.now(); // Current time

        return productHistoryRepository.insertProductHistory(idProductHistory, productId, newProductStateId, previousProductStateId, changeDate, changedBy);
    }
}
