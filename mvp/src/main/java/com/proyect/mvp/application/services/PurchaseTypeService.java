package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.PurchaseTypeEntity;
import com.proyect.mvp.domain.repository.PurchaseTypeRepository;
import com.proyect.mvp.dtos.create.PurchaseTypeCreateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PurchaseTypeService {

    private final PurchaseTypeRepository purchaseTypeRepository;

    public PurchaseTypeService(PurchaseTypeRepository purchaseTypeRepository) {
        this.purchaseTypeRepository = purchaseTypeRepository;
    }

    public Flux<PurchaseTypeEntity> getAllPurchaseTypes() {
        return purchaseTypeRepository.findAll();
    }

    public Mono<PurchaseTypeEntity> getPurchaseTypeById(UUID id) {
        return purchaseTypeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<PurchaseTypeEntity> saveNewPurchaseType(PurchaseTypeCreateDTO purchaseType) {
        PurchaseTypeEntity purchaseTypeEntity = PurchaseTypeEntity.builder()
                .idPurchaseType(UUID.randomUUID())
                .name(purchaseType.getName())
                .build();

        return purchaseTypeRepository.insertPurchaseType(purchaseTypeEntity.getIdPurchaseType(), purchaseTypeEntity.getName())
                .thenReturn(purchaseTypeEntity)
                .onErrorMap(error -> {
                    System.err.println("Error al guardar tipo de compra: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving purchase type", error);
                });
    }


    public Mono<Void> deletePurchaseTypeById(UUID id) {
        return purchaseTypeRepository.findById(id)
                .flatMap(existingPurchaseType -> purchaseTypeRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
