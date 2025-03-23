package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Table("user_history")
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UserHistoryEntity {

    @Id
    @Column("id_user_history")
    private UUID idUserHistory;

    @Column("fk_user")
    private UUID idUser;

    @Column("fk_user_state")
    private UUID idUserState;

    @Column("initial_date")
    private LocalDateTime initialDate;

    @Column("final_date")
    private LocalDateTime finalDate;

    @Column("description")
    private String description;
}