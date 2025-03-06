package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.dtos.create.ONGCreateDTO;
import com.proyect.mvp.domain.model.dtos.update.ONGUpdateDTO;
import com.proyect.mvp.domain.model.entities.ONGEntity;
import com.proyect.mvp.domain.repository.ONGRepository;

import java.util.UUID;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ONGService {

    private final ONGRepository ongRepository;

    public ONGService(ONGRepository ongRepository) {
        this.ongRepository = ongRepository;
    }

    public Mono<ONGEntity> createONG(ONGEntity ong) {
        return ongRepository.save(ong);
    }

    public Mono<ONGEntity> getONG(UUID id) {
        return ongRepository.findById(id);
    }

    public Flux<ONGEntity> getAllONGs() {
        return ongRepository.findAll();
    }

    public Mono<ONGEntity> updateONG(ONGUpdateDTO ongDTO) {
        return ongRepository.findById(ongDTO.getIdOng())
                .flatMap(ong -> {
                    ONGEntity ongActualizada = ONGEntity.builder()
                            .idOng(ong.getIdOng())
                            .name(ongDTO.getName())
                            .account(ongDTO.getAccount())
                            .build();
                    return ongRepository.save(ongActualizada);
                });
    }
}
