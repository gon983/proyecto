package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.SaleHistoryEntity;
import com.proyect.mvp.domain.repository.SaleHistoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SaleHistoryService {

    private final SaleHistoryRepository saleHistoryRepository;

    public SaleHistoryService(SaleHistoryRepository saleHistoryRepository) {
        this.saleHistoryRepository = saleHistoryRepository;
    }

    public Flux<SaleHistoryEntity> getSaleHistory(UUID saleId) {
        return saleHistoryRepository.findBySaleId(saleId);
    }

    public Mono<SaleHistoryEntity> registerSaleStateChange(UUID saleId, UUID newSaleStateId, UUID previousSaleStateId, LocalDateTime initialDate, LocalDateTime finalDate, UUID changedBy) {
        UUID idSaleHistory = UUID.randomUUID();

        return saleHistoryRepository.insertSaleHistory(idSaleHistory, saleId, newSaleStateId, initialDate, finalDate);
    }
}
