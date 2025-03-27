package com.proyect.mvp.infrastructure.security;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Builder
@Getter
public class UserAuthenticationDTO {
    private UUID idUser;
    private String username;
    private String email;
    private String role;
    private String password;


}

 
