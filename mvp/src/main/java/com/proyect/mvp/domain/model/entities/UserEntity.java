package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

import com.proyect.mvp.infrastructure.security.UserAuthenticationDTO;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Transient; // Import correcto para Spring Data R2DBC



import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Table("users") 
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column("id_user")
    private UUID idUser;

    @Column("username")
    private String username;

    @Column("email")
    private String email;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("document_type")
    private String documentType;

    @Column("document_number")
    private String documentNumber;

    @Column("fk_neighborhood")
    private UUID fkNeighborhood;

    @Column("phone")
    private String phone;

    @Nullable
    @Column("photo")
    private String photo;

    

    @Column("level")
    private int level;

    // Campos opcionales
    @Nullable
    @Column("fk_collection_point_suscribed")
    private UUID fkCollectionPointSuscribed;
    
    
    @Column("role")
    private String role;

    @Column("password")
    private String password;
    
    
    
    @Nullable
    @Column("minimal_sale")
    private Double minimalSale;

    @Column("mp_public_key_encrypted")
    private String mpPublicKeyEncrypted;

    @Column("mp_access_token_encrypted")
    private String mpAccessTokenEncrypted;

    @Transient
    private String mpPublicKey;

    @Transient
    private String mpAccessToken;
    @Column("access_token_productor")
    private String accessTokenProductor;
    @Column("user_productor_mp_id")
    private String userProductorMpId;
    @Column("refresh_productor_token")
    private String refreshProductorToken;


    

    public UserAuthenticationDTO toUserAuthenticationDTO(){
        UserAuthenticationDTO dto = UserAuthenticationDTO.builder()
                                                        .idUser(idUser)
                                                        .username(username)
                                                        .email(email)
                                                        .role(role)
                                                        .password(password)
                                                        .build();
        return dto;
    }

 
}
