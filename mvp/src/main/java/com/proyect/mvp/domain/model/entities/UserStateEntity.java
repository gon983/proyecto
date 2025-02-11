package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "userstate")
@Getter
@NoArgsConstructor
public class UserStateEntity {

    @Id
    
    @Column( "id_user_state")
    private String idUserState;

    @Column( "name")
    private String name;
}