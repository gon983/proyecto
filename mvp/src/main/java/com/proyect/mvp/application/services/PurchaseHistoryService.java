package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.PurchaseHistoryEntity;
import com.proyect.mvp.domain.repository.PurchaseHistoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PurchaseHistoryService {

    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public PurchaseHistoryService(PurchaseHistoryRepository purchaseHistoryRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    public Flux<PurchaseHistoryEntity> getPurchaseHistory(UUID purchaseId) {
        return purchaseHistoryRepository.findByPurchaseId(purchaseId);
    }

    public Mono<PurchaseHistoryEntity> registerPurchaseStateChange(UUID purchaseId, UUID newPurchaseStateId, UUID previousPurchaseStateId, LocalDateTime initialDate, LocalDateTime finalDate, UUID changedBy) {
        UUID idPurchaseHistory = UUID.randomUUID();

        return purchaseHistoryRepository.insertPurchaseHistory(idPurchaseHistory, purchaseId, newPurchaseStateId, previousPurchaseStateId, initialDate, finalDate, changedBy);
    }
}