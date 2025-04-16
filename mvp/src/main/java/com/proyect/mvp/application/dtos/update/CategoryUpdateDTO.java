package com.proyect.mvp.application.dtos.update;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;

import lombok.Getter;

@Getter
public class CategoryUpdateDTO {
   
    private UUID idCategory;

  
    private String name;

   
    private String photo;
    
}
