package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.ProductStateEntity;
import com.proyect.mvp.domain.repository.ProductStateRepository;
import com.proyect.mvp.dtos.create.ProductStateCreateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductStateService {

    private final ProductStateRepository productStateRepository;

    public ProductStateService(ProductStateRepository productStateRepository) {
        this.productStateRepository = productStateRepository;
    }

    public Flux<ProductStateEntity> getAllProductStates() {
        return productStateRepository.findAll();
    }

    public Mono<ProductStateEntity> getProductStateById(UUID id) {
        return productStateRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<ProductStateEntity> saveNewProductState(ProductStateCreateDTO productState) {
        ProductStateEntity productStateEntity = ProductStateEntity.builder()
                .idProductState(UUID.randomUUID()) // Generar UUID aquí
                .name(productState.getName())
                .build();

        return productStateRepository.insertProductState(productStateEntity.getIdProductState(), productStateEntity.getName())
                .thenReturn(productStateEntity)
                .onErrorMap(error -> {
                    System.err.println("Error al guardar estado de producto: " + error.getMessage());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving product state", error);
                });
    }


    public Mono<Void> deleteProductStateById(UUID id) {
        return productStateRepository.findById(id)
                .flatMap(existingProductState -> productStateRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
