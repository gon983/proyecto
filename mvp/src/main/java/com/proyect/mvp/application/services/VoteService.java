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

import com.proyect.mvp.domain.model.entities.VoteEntity;

import com.proyect.mvp.domain.repository.VoteRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VoteService {


    private final VoteRepository voteRepository;


    public VoteService(VoteRepository voteRepository){
        this.voteRepository = voteRepository;

        

    }

    public Mono<VoteEntity> voteProduct(VoteCreateDTO voteDto, UUID fkUser) {
        VoteEntity vote = VoteEntity.builder()
                                    .fkProduct(voteDto.getFkProduct())
                                    .comment(voteDto.getComment())
                                    .fkUser(fkUser)
                                    .date(OffsetDateTime.now())
                                    .calification(voteDto.getCalification())
                                    .build();
        
        return voteRepository.save(vote);
                      
    }

  

    private OffsetDateTime getCollectionDay(OffsetDateTime now, DayOfWeek collectionDay) {
        return now.with(collectionDay).withHour(0).withMinute(0).withSecond(0);
    }

    


    public Mono<Boolean> validateUserDontVoteYet(UUID fkDpCp , UUID idUser){
        return voteRepository.findVoteByFkUserAndFkDefaultProductCollectionPointWeek(idUser, fkDpCp)
                            .map(found -> false)  // Si encuentra algo, el usuario ya votó
                            .defaultIfEmpty(true);

    }


    
    
    
}
