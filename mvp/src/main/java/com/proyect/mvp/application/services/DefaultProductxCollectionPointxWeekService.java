package com.proyect.mvp.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.proyect.mvp.application.dtos.create.DefaultProductxCollectionPointxWeekCreateDTO;
import com.proyect.mvp.domain.model.entities.DefaultProductxCollectionPointxWeekEntity;
import com.proyect.mvp.domain.model.entities.ProductEntity;
import com.proyect.mvp.domain.repository.DefaultProductxCollectionPointxWeekRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DefaultProductxCollectionPointxWeekService {
    private final DefaultProductxCollectionPointxWeekRepository pxCpRepository;
    private final ProductService productService;
    private final UserService userService;

    public DefaultProductxCollectionPointxWeekService(DefaultProductxCollectionPointxWeekRepository pxCpRepository, ProductService productService, UserService userService){
        this.pxCpRepository = pxCpRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public Flux<DefaultProductxCollectionPointxWeekEntity> findAllFromCollectionPointAndDate(UUID fkCollectionPoint){
        return pxCpRepository.findAllByFkCollectionPoint(fkCollectionPoint); //La implementacion de la date queda pendiente
        
    } 


    public Flux<ProductEntity> getAllProductsForCpWithLevelPrice(UUID idUser) {
    return userService.getUserById(idUser)
            .flatMapMany(user -> 
                pxCpRepository.findAllByFkCollectionPoint(user.getFkCollectionPointSuscribed())
                    .flatMap(defaultProductxCp -> 
                        productService.getProductById(defaultProductxCp.getFkProduct())
                            .map(product -> {
                                product.calculatePrice(user.getLevel()); // Ahora user está disponible
                                return product;
                            })
                    )
            );
    }

    

    public Mono<DefaultProductxCollectionPointxWeekEntity> insertDefaultProductxCollectionPointxWeek(UUID fkCollectionPoint, UUID fkStandarProduct, UUID fkProduct){
        DefaultProductxCollectionPointxWeekEntity defaultProductxCp = 
        DefaultProductxCollectionPointxWeekEntity.builder()
                                                .fkCollectionPoint(fkCollectionPoint)
                                                .fkStandarProduct(fkStandarProduct)
                                                .fkProduct(fkProduct)
                                                .build();
        return pxCpRepository.save(defaultProductxCp);

        
    }
    

    
    
}
