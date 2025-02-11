package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "country")
@Getter
@Setter
@NoArgsConstructor
public class CountryEntity{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_country")
    private String idCountry;

    @Column(name = "name")
    private String name;

}