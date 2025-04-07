package com.proyect.mvp.application.services;

import com.proyect.mvp.application.dtos.create.PurchaseDetailStateCreateDTO;
import com.proyect.mvp.domain.model.entities.PurchaseDetailStateEntity;
import com.proyect.mvp.domain.repository.PurchaseDetailStateRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PurchaseDetailStateService {

    private final PurchaseDetailStateRepository purchaseDetailStateRepository;

    public PurchaseDetailStateService(PurchaseDetailStateRepository purchaseDetailStateRepository) {
        this.purchaseDetailStateRepository = purchaseDetailStateRepository;
    }

    public Flux<PurchaseDetailStateEntity> getAllPurchaseDetailStates() {
        return purchaseDetailStateRepository.findAll();
    }

    public Mono<PurchaseDetailStateEntity> getPurchaseDetailStateById(UUID id) {
        return purchaseDetailStateRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<PurchaseDetailStateEntity> saveNewPurchaseDetailState(PurchaseDetailStateCreateDTO purchaseDetailState) {
        PurchaseDetailStateEntity purchaseDetailStateEntity = PurchaseDetailStateEntity.builder()
                .idPurchaseDetailState(UUID.randomUUID())
                .name(purchaseDetailState.getName())
                .build();

        return purchaseDetailStateRepository.insertPurchaseDetailState(purchaseDetailStateEntity.getIdPurchaseDetailState(), purchaseDetailStateEntity.getName())
                .thenReturn(purchaseDetailStateEntity)
                .onErrorMap(error -> {
                    System.err.println("Error al guardar estado de detalle de compra: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving purchase detail state", error);
                });
    }


    public Mono<Void> deletePurchaseDetailStateById(UUID id) {
        return purchaseDetailStateRepository.findById(id)
                .flatMap(existingPurchaseDetailState -> purchaseDetailStateRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<PurchaseDetailStateEntity> findByName(String name){
        return purchaseDetailStateRepository.findOneByName(name)
                                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));


    }

    public Mono<Boolean> isDetailInPending(UUID idDetailState){
        return purchaseDetailStateRepository.findOneByName("pending")
                                            .flatMap(state -> Mono.just(state.getIdPurchaseDetailState().equals(idDetailState)));
        

    }
}