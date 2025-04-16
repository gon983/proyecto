package com.proyect.mvp.application.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyect.mvp.application.dtos.create.CategoryCreateDTO;
import com.proyect.mvp.application.dtos.update.CategoryUpdateDTO;
import com.proyect.mvp.domain.model.entities.CategoryEntity;
import com.proyect.mvp.domain.repository.CategoryRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Flux<CategoryEntity> getAllCategories(){
        return categoryRepository.findAll();
        
    }

    public Mono<CategoryEntity> putCategory(CategoryUpdateDTO dto){
        return categoryRepository.findById(dto.getIdCategory())
                                 .flatMap(existingCategory ->
                                 {
                                    CategoryEntity entity = CategoryEntity.builder()
                                                                          .idCategory(existingCategory.getIdCategory())
                                                                          .name(dto.getName())
                                                                          .photo(dto.getPhoto())
                                                                          .build();
                                    return categoryRepository.save(entity);
                                 });
    }

    public Mono<CategoryEntity> getCategoryById(UUID id){
        return categoryRepository.findById(id)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
                                
    }
    
    public Mono<CategoryEntity> saveNewCategory(CategoryCreateDTO categoryDTO){
        CategoryEntity category = CategoryEntity.builder()
                                                .name(categoryDTO.getName())
                                                .build();
        return categoryRepository.save(category)
                                .thenReturn(category)
                                .onErrorMap(error->{return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving category", error);} );

    }
}
