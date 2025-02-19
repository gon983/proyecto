package com.proyect.mvp.dtos.create;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserCreateDTO {
  



 
    private String username;

   
    private String email;

    
    private LocalDateTime createdAt;

    private String firstName;

    private String lastName;

   
    private String documentType;

    
    private String documentNumber;

    
    private UUID fkNeighborhood;

    private String phone;

    
    private String photo;

    private UUID roleOne;
}
