package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.proyect.mvp.domain.model.entities.CategoryEntity;

import reactor.core.publisher.Mono;

public interface CategoryRepository extends R2dbcRepository<CategoryEntity, UUID>{
    // @Query("INSERT INTO category (id_category, name)) VALUES :#{#category.idCategory}, :#{#category.name} ")
    // Mono<CategoryEntity> insertCategory(@Param("category")CategoryEntity category);
    
}
