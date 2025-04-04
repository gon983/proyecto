package com.proyect.mvp.application.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    String username;
    String password;
    

    public void imprimir(){
        System.out.println(username);
        System.out.println(password);
        
    }
}
