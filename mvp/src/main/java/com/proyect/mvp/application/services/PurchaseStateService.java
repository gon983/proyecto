package com.proyect.mvp.application.services;

import com.proyect.mvp.application.dtos.create.PurchaseStateCreateDTO;
import com.proyect.mvp.domain.model.entities.PurchaseStateEntity;
import com.proyect.mvp.domain.repository.PurchaseStateRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PurchaseStateService {

    private final PurchaseStateRepository purchaseStateRepository;

    public PurchaseStateService(PurchaseStateRepository purchaseStateRepository) {
        this.purchaseStateRepository = purchaseStateRepository;
    }

    public Flux<PurchaseStateEntity> getAllPurchaseStates() {
        return purchaseStateRepository.findAll();
    }

    public Mono<PurchaseStateEntity> getPurchaseStateById(UUID id) {
        return purchaseStateRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<PurchaseStateEntity> saveNewPurchaseState(PurchaseStateCreateDTO purchaseState) {
        PurchaseStateEntity purchaseStateEntity = PurchaseStateEntity.builder()
                .idPurchaseState(UUID.randomUUID())
                .name(purchaseState.getName())
                .build();

        return purchaseStateRepository.insertPurchaseState(purchaseStateEntity.getIdPurchaseState(), purchaseStateEntity.getName())
                .thenReturn(purchaseStateEntity)
                .onErrorMap(error -> {
                    System.err.println("Error al guardar estado de compra: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving purchase state", error);
                });
    }


    public Mono<Void> deletePurchaseStateById(UUID id) {
        return purchaseStateRepository.findById(id)
                .flatMap(existingPurchaseState -> purchaseStateRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }


    public Mono<PurchaseStateEntity> findByName(String name) {
        return purchaseStateRepository.findOneByName(name)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<Boolean> isPurchaseInPending(UUID idPurchaseState){
        
        return purchaseStateRepository.findOneByName("pending")
                                            
                                            .flatMap(state -> Mono.just(state.getIdPurchaseState().equals(idPurchaseState)));
        

    }
}