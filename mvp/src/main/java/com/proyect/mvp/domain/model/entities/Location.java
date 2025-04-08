package com.proyect.mvp.domain.model.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.locationtech.jts.geom.Point;

import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Table("locations")
public class Location {

    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("address")
    private String address;


    @Column("coordinates")
    private Point coordinates;



}
