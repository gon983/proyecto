package com.proyect.mvp.domain.model.dtos.update;





import java.util.UUID;

import lombok.Getter;

@Getter
public class DefaultDonationUpdateDTO {
    private UUID idDefaultDonation;
    private UUID fkOrganization;
    private int level;
}
