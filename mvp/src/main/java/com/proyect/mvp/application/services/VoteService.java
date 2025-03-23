package com.proyect.mvp.application.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                                    .calification(voteDto.getCalification())
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
                    return countVotesAndSelectProduct(collectionPoint.getIdCollectionPoint());
                }
                return Flux.empty();
            })            .subscribe();
    }

    private OffsetDateTime getCollectionDay(OffsetDateTime now, DayOfWeek collectionDay) {
        return now.with(collectionDay).withHour(0).withMinute(0).withSecond(0);
    }

    public Flux<DefaultProductxCollectionPointxWeekEntity> countVotesAndSelectProduct(UUID idCollectionPoint) {    
        return dpxcpService.getAllDefaultProductsxCpxWeekToVote(idCollectionPoint)
                .flatMap(defaultProduct -> 
                    voteRepository.getMostVotedProductForDefaultProduct(defaultProduct.getIdDefaultProductxCollectionPoint())
                        .doOnNext(mostVoted -> {
                            
                            defaultProduct.setFkProduct(mostVoted);
                            
                        })
                        .thenReturn(defaultProduct)
                )
                .flatMap(defaultProduct -> dpxcpService.update(defaultProduct)); // Ahora encadena la actualización
    }

    public Mono<DefaultProductxCollectionPointxWeekEntity> calificateProduct(VoteCreateDTO voteDto, UUID idUser) {
        return validateUserDontVoteYet(voteDto.getFkDefaultProductCollectionPointWeek(), idUser)
                .filter(Boolean::booleanValue)
                .flatMap(canVote -> voteProduct(voteDto, idUser)
                        .flatMap(savedVote -> dpxcpService.updateCalification(
                                voteDto.getFkDefaultProductCollectionPointWeek(), 
                                voteDto.getCalification())))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "User already voted for this product")));
    }

    public Mono<Boolean> validateUserDontVoteYet(UUID fkDpCp , UUID idUser){
        return voteRepository.findVoteByFkUserAndFkDefaultProductCollectionPointWeek(idUser, fkDpCp)
                            .map(found -> false)  // Si encuentra algo, el usuario ya votó
                            .defaultIfEmpty(true);

    }


    
    
    
}
