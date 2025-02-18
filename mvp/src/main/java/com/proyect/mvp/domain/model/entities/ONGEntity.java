package com.proyect.mvp.domain.model.entities;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ong")
@Getter
@Builder
public class ONGEntity {

    @Id
    @Column("id_ong")
    private final UUID idOng;

    @Column("name")
    private final String name;

    @Column("account")
    private final String account;
}
