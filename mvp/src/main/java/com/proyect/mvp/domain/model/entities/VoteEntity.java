package com.proyect.mvp.domain.model.entities;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
@Table("vote")
public class VoteEntity {

    @Id
    @Column("id_vote")
    private UUID idVote;

    @Column("fk_product")
    private UUID fkProduct;

    @Column("fk_user")
    private UUID fkUser;

    @Column("date")
    private OffsetDateTime date;

    @Column("comment")
    private String comment;

    @Column("fk_default_product_x_collection_point_x_week")
    private UUID fkDefaultProductCollectionPointWeek;
}

