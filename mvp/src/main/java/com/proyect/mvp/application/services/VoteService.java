package com.proyect.mvp.application.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.proyect.mvp.application.dtos.create.VoteCreateDTO;
import com.proyect.mvp.domain.model.entities.DefaultProductxCollectionPointxWeekEntity;
import com.proyect.mvp.domain.model.entities.VoteEntity;
import com.proyect.mvp.domain.repository.ONGRepository;
import com.proyect.mvp.domain.repository.VoteRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VoteService {

    private final ONGRepository ONGRepository;
    private final VoteRepository voteRepository;
    private final CollectionPointService collectionPointService;
    private final DefaultProductxCollectionPointxWeekService dpxcpService;

    public VoteService(VoteRepository voteRepository, CollectionPointService collectionPointService, ONGRepository ONGRepository, DefaultProductxCollectionPointxWeekService dpxcpService){
        this.voteRepository = voteRepository;
        this.collectionPointService = collectionPointService;
        this.ONGRepository = ONGRepository;
        this.dpxcpService = dpxcpService;
        

    }

    public Mono<VoteEntity> voteProduct(VoteCreateDTO voteDto, UUID fkUser) {
        VoteEntity vote = VoteEntity.builder()
                                    .fkProduct(voteDto.getFkProduct())
                                    .comment(voteDto.getComment())
                                    .fkUser(fkUser)
                                    .fkDefaultProductCollectionPointWeek(voteDto.getFkDefaultProductCollectionPointWeek())
                                    .date(OffsetDateTime.now())
                                    .build();
        
        return voteRepository.save(vote);
                      
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void processProductRenewalByVotation(){
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        collectionPointService.getAllCollectionPoints()
            .flatMap(collectionPoint -> {
                OffsetDateTime collectionDay = getCollectionDay(now, collectionPoint.getCollectionRecurrentDay());
                OffsetDateTime processDay = collectionDay.plusDays(3); 
                LocalDate today = LocalDate.now();

                if (today.equals(processDay.toLocalDate())) {
                    return countVotesAndSelectProduct(collectionPoint.getIdCollectionPoint(), collectionDay.plusDays(2), collectionDay.plusDays(3));
                }
                return Flux.empty();
            })            .subscribe();
    }

    private OffsetDateTime getCollectionDay(OffsetDateTime now, DayOfWeek collectionDay) {
        return now.with(collectionDay).withHour(0).withMinute(0).withSecond(0);
    }

    private Flux<DefaultProductxCollectionPointxWeekEntity> countVotesAndSelectProduct(UUID idCollectionPoint, OffsetDateTime startDate, OffsetDateTime finishDate) {    
        return dpxcpService.getAllDefaultProductsxCpxWeekToVote(idCollectionPoint) // Flux<DefaultProduct>
                .flatMap(defaultProduct -> 
                    voteRepository.getMostVotedProductForDefaultProduct(defaultProduct.getIdDefaultProductxCollectionPoint())
                        .doOnNext(defaultProduct::setFkProduct) // Asignamos sin alterar la cadena reactiva
                        .thenReturn(defaultProduct) // Pasamos el objeto modificado
                )
                .flatMap(defaultProduct -> {
                    dpxcpService.update(defaultProduct); // Guardamos el cambio en la BD
                    return Mono.just(defaultProduct);
                });
    }
    
    
}
