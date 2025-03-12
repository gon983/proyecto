package com.proyect.mvp.application.services;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.proyect.mvp.application.dtos.create.VoteCreateDTO;
import com.proyect.mvp.domain.model.entities.VoteEntity;
import com.proyect.mvp.domain.repository.VoteRepository;

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
                                    .fkDefaultProductCollectionPointWeek(voteDto.getFkDefaultProductCollectionPointWeek())
                                    .date(OffsetDateTime.now())
                                    .build();
        
        return voteRepository.save(vote);
                      
    }
    
}
