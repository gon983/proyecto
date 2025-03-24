package com.proyect.mvp.application.dtos.requests;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductsPayedDTO {
    List<UUID> productsPayed;
    
}
