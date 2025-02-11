package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "neighborhood")
@Getter
@NoArgsConstructor
public class NeighborhoodEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_neighborhood")
    private String idNeighborhood;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_locality")
    private LocalityEntity  locality; 
}