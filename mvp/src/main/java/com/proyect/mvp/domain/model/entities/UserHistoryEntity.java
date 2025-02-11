package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;


@Table( "userhistory")
@Getter
@NoArgsConstructor
public class UserHistoryEntity {

    @Id
    
    @Column( "id_user_history")
    private String idUserHistory;

    
    
    private UserEntity user;

    
    
    private UserStateEntity userState;

    @Column( "description")
    private String description;

    @Column( "init")
    private LocalDateTime init;

    @Column( "finish")
    private LocalDateTime finish;
}