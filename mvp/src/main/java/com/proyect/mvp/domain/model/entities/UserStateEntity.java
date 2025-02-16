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
@AllArgsConstructor
@Table("userstate")
@Getter
@NoArgsConstructor
public class UserStateEntity {

    @Id
    @Column("id_user_state")
    private UUID idUserState;

    @Column("name")
    private String name;

    public UserStateEntity(String name) {
        this.idUserState = UUID.randomUUID();
        this.name = name;
    }
}
