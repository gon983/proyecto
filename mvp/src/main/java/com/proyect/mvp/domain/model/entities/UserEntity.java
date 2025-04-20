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
    
    
    @Column("role")
    private String role;

    @Column("password")
    private String password;

    @Column("unread")
    private Boolean unread;
    
    
    
   

    

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
