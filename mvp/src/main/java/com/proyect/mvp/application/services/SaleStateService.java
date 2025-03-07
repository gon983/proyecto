package com.proyect.mvp.application.services;

import com.proyect.mvp.application.dtos.create.SaleStateCreateDTO;
import com.proyect.mvp.domain.model.entities.SaleStateEntity;
import com.proyect.mvp.domain.repository.SaleStateRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class SaleStateService {

    private final SaleStateRepository saleStateRepository;

    public SaleStateService(SaleStateRepository saleStateRepository) {
        this.saleStateRepository = saleStateRepository;
    }

    public Flux<SaleStateEntity> getAllSaleStates() {
        return saleStateRepository.findAll();
    }

    public Mono<SaleStateEntity> getSaleStateById(UUID id) {
        return saleStateRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<SaleStateEntity> saveNewSaleState(SaleStateCreateDTO saleState) {
        SaleStateEntity saleStateEntity = SaleStateEntity.builder()
                .idSaleState(UUID.randomUUID())
                .name(saleState.getName())
                .build();

        return saleStateRepository.insertSaleState(saleStateEntity.getIdSaleState(), saleStateEntity.getName())
                .thenReturn(saleStateEntity)
                .onErrorMap(error -> {
                    System.err.println("Error al guardar estado de venta: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving sale state", error);
                });
    }


    public Mono<Void> deleteSaleStateById(UUID id) {
        return saleStateRepository.findById(id)
                .flatMap(existingSaleState -> saleStateRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
