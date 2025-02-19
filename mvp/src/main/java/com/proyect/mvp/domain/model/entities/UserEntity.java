package com.proyect.mvp.domain.model.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Table( "user") 
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column( "id_user")
    private UUID idUser;

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

    
    @Column("fk_neighborhood")
    private UUID fkNeighborhood;

    @Column( "phone")
    private String phone;

    @Column( "photo")
    private String photo;

    @Column("fk_role_one")
    private UUID roleOne;
    

    // Hasta aca los campos obligatorios
    @Column("fk_role_two")
    private UUID roleTwo;
    @Column("fk_role_three")
    private UUID roleThree;
    @Column("minimal_sale")
    private double minimalSale;
    

}