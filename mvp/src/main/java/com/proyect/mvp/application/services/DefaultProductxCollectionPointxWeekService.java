package com.proyect.mvp.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.proyect.mvp.domain.model.entities.DefaultProductxCollectionPointxWeekEntity;
import com.proyect.mvp.domain.model.entities.ProductEntity;
import com.proyect.mvp.domain.repository.DefaultProductxCollectionPointxWeekRepository;
import com.proyect.mvp.dtos.create.DefaultProductxCollectionPointxWeekCreateDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DefaultProductxCollectionPointxWeekService {
    private final DefaultProductxCollectionPointxWeekRepository pxCpRepository;
    private final ProductService productService;

    public DefaultProductxCollectionPointxWeekService(DefaultProductxCollectionPointxWeekRepository pxCpRepository, ProductService productService){
        this.pxCpRepository = pxCpRepository;
        this.productService = productService;

    }

    public Flux<DefaultProductxCollectionPointxWeekEntity> findAllFromCollectionPointAndDate(UUID fkCollectionPoint){
        return pxCpRepository.findAllByFkCollectionPoint(fkCollectionPoint); //La implementacion de la date queda pendiente
        
    } 


    public Flux<ProductEntity> getAllProductsForCpWithLevelPrice(UUID fkCollectionPoint) {
        return pxCpRepository.findAllByFkCollectionPoint(fkCollectionPoint)
                .flatMap(defaultProductxCp -> productService.getProductById(defaultProductxCp.getFkProduct()));
    }

    public Mono<DefaultProductxCollectionPointxWeekEntity> createDefaultProductxCollectionPointxWeek(DefaultProductxCollectionPointxWeekCreateDTO defaultProductxCpDto){
        DefaultProductxCollectionPointxWeekEntity defaultProductxCp = 
        DefaultProductxCollectionPointxWeekEntity.builder()
                                                .fkCollectionPoint(defaultProductxCpDto.getFkCollectionPoint())
                                                .fkStandarProduct(defaultProductxCpDto.getFkStandarProduct())
                                                .fkProduct(defaultProductxCpDto.getFkProduct())
                                                .build();
        return pxCpRepository.save(defaultProductxCp);

        
    }
    

    
    
}
