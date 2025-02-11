package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;


@Table( "country")
@Getter
@Setter
@NoArgsConstructor
public class CountryEntity{

    @Id
    
    @Column( "id_country")
    private String idCountry;

    @Column( "name")
    private String name;

}