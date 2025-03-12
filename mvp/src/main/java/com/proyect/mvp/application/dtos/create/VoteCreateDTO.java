package com.proyect.mvp.application.dtos.create;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;

@Getter
public class VoteCreateDTO {
    private UUID fkProduct;
    private UUID fkUser;
    private String comment;
    private UUID fkDefaultProductCollectionPointWeek;
    
}
