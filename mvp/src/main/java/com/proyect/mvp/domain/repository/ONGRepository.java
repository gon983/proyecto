package com.proyect.mvp.domain.repository;



import com.proyect.mvp.domain.model.entities.ONGEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ONGRepository extends R2dbcRepository<ONGEntity, UUID> {
    // Puedes agregar consultas personalizadas aquí si es necesario
}
