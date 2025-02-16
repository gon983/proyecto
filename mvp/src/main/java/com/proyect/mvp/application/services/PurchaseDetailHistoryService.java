package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.PurchaseDetailHistoryEntity;
import com.proyect.mvp.domain.repository.PurchaseDetailHistoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PurchaseDetailHistoryService {

    private final PurchaseDetailHistoryRepository purchaseDetailHistoryRepository;

    public PurchaseDetailHistoryService(PurchaseDetailHistoryRepository purchaseDetailHistoryRepository) {
        this.purchaseDetailHistoryRepository = purchaseDetailHistoryRepository;
    }

    public Flux<PurchaseDetailHistoryEntity> getPurchaseDetailHistory(UUID purchaseDetailId) {
        return purchaseDetailHistoryRepository.findByPurchaseDetailId(purchaseDetailId);
    }

    public Mono<PurchaseDetailHistoryEntity> registerPurchaseDetailStateChange(UUID purchaseDetailId, UUID newPurchaseDetailStateId, UUID previousPurchaseDetailStateId, LocalDateTime initialDate, LocalDateTime finalDate) {
        UUID idPurchaseDetailHistory = UUID.randomUUID();

        return purchaseDetailHistoryRepository.insertPurchaseDetailHistory(idPurchaseDetailHistory, purchaseDetailId, newPurchaseDetailStateId, previousPurchaseDetailStateId, initialDate, finalDate);
    }
}
