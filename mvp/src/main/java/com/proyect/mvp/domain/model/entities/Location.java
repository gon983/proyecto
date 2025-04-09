package com.proyect.mvp.domain.model.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.locationtech.jts.geom.Point;

import java.util.UUID;
import java.time.LocalDateTime;

@Getter
@Setter
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

    @Column("active")
    private boolean active;



}
