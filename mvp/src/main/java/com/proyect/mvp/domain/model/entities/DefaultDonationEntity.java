package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Builder
@Table("default_donation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultDonationEntity {

    @Id
    @Column("id_default_donation")
    private UUID idDefaultDonation; // UUID

    @Column("fk_organization")
    private UUID fkOrganization; // UUID

    @Column("level")
    private int level; // int (1-7)
}