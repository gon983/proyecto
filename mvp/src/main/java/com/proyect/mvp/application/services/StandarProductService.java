package com.proyect.mvp.application.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyect.mvp.application.dtos.create.StandarProductCreateDTO;
import com.proyect.mvp.domain.model.entities.StandarProductEntity;
import com.proyect.mvp.domain.repository.StandarProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service 
public class StandarProductService {
    private final StandarProductRepository standarProductRepository;

    public StandarProductService(StandarProductRepository standarProductRepository) {
        this.standarProductRepository = standarProductRepository;
    }
    

    public Flux<StandarProductEntity> getAllStandarProducts() {
        return standarProductRepository.findAll();
    }

    public Mono<StandarProductEntity> getStandarProductById(UUID id) {
        return standarProductRepository.findById(id)
                                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<StandarProductEntity> createStandarProduct(StandarProductCreateDTO standarProductDto){
        StandarProductEntity standarProduct = StandarProductEntity.builder()
                .name(standarProductDto.getName())  
                .fkCategory(standarProductDto.getFkCategory())
                .build();
        return standarProductRepository.save(standarProduct);
    }

    


    

    
}
