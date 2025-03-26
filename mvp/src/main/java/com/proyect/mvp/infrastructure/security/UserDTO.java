package com.proyect.mvp.infrastructure.security;

import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
    private List<String> roles;
    private String password;


}

 
