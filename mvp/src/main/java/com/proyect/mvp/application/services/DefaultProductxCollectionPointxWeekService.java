package com.proyect.mvp.application.services;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CollectionPointService collectionPointService;

    public DefaultProductxCollectionPointxWeekService(DefaultProductxCollectionPointxWeekRepository pxCpRepository, ProductService productService, UserService userService, CollectionPointService collectionPointService){
        this.pxCpRepository = pxCpRepository;
        this.productService = productService;
        this.userService = userService;
        this.collectionPointService = collectionPointService;
    }

    // public Flux<DefaultProductxCollectionPointxWeekEntity> findAllFromCollectionPointAndDate(UUID fkCollectionPoint){
    //     return pxCpRepository.findAllByFkCollectionPoint(fkCollectionPoint); //La implementacion de la date queda pendiente
        
    // } 


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

    

    

    // @Scheduled(cron = "0 0 12 * * *") // Se ejecuta todos los días a las 12:00
    // @Transactional
    public void processProductRenewal() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        collectionPointService.getAllCollectionPoints()
            .flatMap(collectionPoint -> {
                OffsetDateTime collectionDay = getCollectionDay(now, collectionPoint.getCollectionRecurrentDay());
                OffsetDateTime processDay = collectionDay.plusDays(2); 
                LocalDate today = LocalDate.now();

                if (today.equals(processDay.toLocalDate())) {
                    return renewProductsForCollectionPoint(collectionPoint.getIdCollectionPoint());
                }
                return Flux.empty();
            })
            .subscribe();
    }

    private OffsetDateTime getCollectionDay(OffsetDateTime now, DayOfWeek collectionDay) {
        return now.with(collectionDay).withHour(0).withMinute(0).withSecond(0);
    }

    public Flux<Void> renewProductsForCollectionPoint(UUID collectionPointId) {
        return pxCpRepository.findAllByFkCollectionPointAndDateRange(collectionPointId, OffsetDateTime.now().minusDays(8), OffsetDateTime.now().minusDays(4))
                            .flatMap(defaultProductsWeek -> {
                                    if (defaultProductsWeek.getRating() > 3.5) {
                                    return insertDefaultProductxCollectionPointxWeek(collectionPointId, defaultProductsWeek.getFkProduct(), defaultProductsWeek.getFkStandarProduct());
                                    } else {
                                    // Lógica para abrir votación (en otro módulo)
                                    return createDefaultProductxCollectionPointxWeekWithoutProduct(collectionPointId, defaultProductsWeek.getFkStandarProduct());
                                    }
            })
            .thenMany(Flux.empty());
    }

    public Mono<DefaultProductxCollectionPointxWeekEntity> insertDefaultProductxCollectionPointxWeek(UUID fkCollectionPoint, UUID fkStandarProduct, UUID fkProduct){
        DefaultProductxCollectionPointxWeekEntity defaultProductxCp = 
        DefaultProductxCollectionPointxWeekEntity.builder()
                                                .fkCollectionPoint(fkCollectionPoint)
                                                .fkStandarProduct(fkStandarProduct)
                                                .fkProduct(fkProduct)
                                                .dateRenewalDefaultProducts(OffsetDateTime.now())
                                                .build();
        return pxCpRepository.save(defaultProductxCp);

        
    }

    public Mono<DefaultProductxCollectionPointxWeekEntity> createDefaultProductxCollectionPointxWeekWithoutProduct(UUID fkCollectionPoint, UUID fkStandarProduct){
        DefaultProductxCollectionPointxWeekEntity defaultProductxCp = 
        DefaultProductxCollectionPointxWeekEntity.builder()
                                                .fkCollectionPoint(fkCollectionPoint)
                                                .fkStandarProduct(fkStandarProduct)
                                                .dateRenewalDefaultProducts(OffsetDateTime.now())
                                                .build();
        return pxCpRepository.save(defaultProductxCp);

        
    }

    public Flux<DefaultProductxCollectionPointxWeekEntity> getAllDefaultProductsxCpxWeekToVote(UUID fkCollectionPoint){
        return pxCpRepository.findAllWhereDateIsNearWithFkCollectionPointAndFkProductNull(fkCollectionPoint, OffsetDateTime.now().minusDays(4), OffsetDateTime.now());
    }
    
    

    
    
}
