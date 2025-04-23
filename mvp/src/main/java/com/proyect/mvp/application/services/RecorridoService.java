package com.proyect.mvp.application.services;



import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyect.mvp.application.dtos.create.RecorridoCreateDTO;
import com.proyect.mvp.application.dtos.update.RecorridoUpdateDTO;
import com.proyect.mvp.domain.model.entities.RecorridoEntity;
import com.proyect.mvp.domain.repository.RecorridoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RecorridoService {
    private final RecorridoRepository recorridoRepository;
    private final PurchaseService purchaseService;

    public RecorridoService(RecorridoRepository recorridoRepository, PurchaseService purchaseService) {
        this.recorridoRepository = recorridoRepository;
        this.purchaseService = purchaseService;
    }

    public Flux<RecorridoEntity> getAllRecorridosActivos() {
        return recorridoRepository.findByActiveTrue();
    }

    public Mono<RecorridoEntity> putRecorrido(RecorridoUpdateDTO dto) {
        return recorridoRepository.findById(dto.getIdRecorrido())
                                  .flatMap(existingRecorrido -> {
                                      RecorridoEntity entity = RecorridoEntity.builder()
                                                                             .idRecorrido(existingRecorrido.getIdRecorrido())
                                                                             .name(dto.getName())
                                                                        
                                                                             .cantidadKm(dto.getCantidadKm())
                                                                             .build();
                                      return recorridoRepository.save(entity);
                                  })
                                  .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Recorrido not found")));
    }

    public Mono<RecorridoEntity> getRecorridoById(UUID id) {
        return recorridoRepository.findById(id)
                                 .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Recorrido not found")));
    }
    
    public Mono<RecorridoEntity> saveNewRecorrido(RecorridoCreateDTO recorridoDTO) {
        RecorridoEntity recorrido = RecorridoEntity.builder()
                                                  .name(recorridoDTO.getName())
                                                  .active(true)
                                                  .cantidadKm(0)
                                                  .fecha(ZonedDateTime.now())
                                                  .build();
        return recorridoRepository.save(recorrido)
                                 .thenReturn(recorrido)
                                 .onErrorMap(error -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving recorrido", error));
    }
    
    public Mono<Void> finalizarRecorrido(UUID id) {
        return recorridoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Recorrido not found")))
                .flatMap(recorrido -> {
                    recorrido.setActive(false);
                    return recorridoRepository.save(recorrido).then(purchaseService.finalizarCompras(id)); // .then() devuelve un Mono<Void>
                });
    }
}