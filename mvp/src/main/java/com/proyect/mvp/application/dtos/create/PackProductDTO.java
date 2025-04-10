package com.proyect.mvp.application.dtos.create;



import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class PackProductDTO {
    private UUID productId;
    private Double quantity;
}
