package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;


@Table( "user") 
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    
    @Column( "id_user")
    private String idUser;

    @Column( "username")
    private String username;

    @Column( "email")
    private String email;

    @Column( "created_at")
    private LocalDateTime createdAt;

    @Column( "first_name")
    private String firstName;

    @Column( "last_name")
    private String lastName;

    @Column( "document_type")
    private String documentType;

    @Column( "document_number")
    private String documentNumber;

    
    
    private NeighborhoodEntity neighborhood;

    @Column( "phone")
    private String phone;

    @Column( "photo")
    private String photo;
}