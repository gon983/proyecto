package com.proyect.mvp.domain.model.dtos.create;

import java.util.UUID;

import lombok.Getter;

@Getter
public class DefaultDonationCreateDTO {
    private UUID fkOrganization;
    private int level;
}
