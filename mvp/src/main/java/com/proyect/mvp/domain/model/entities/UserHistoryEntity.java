package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;


@Table( "userhistory")
@Getter
@NoArgsConstructor
public class UserHistoryEntity {

    @Id
    
    @Column( "id_user_history")
    private String idUserHistory;

    
    (name = "fk_user")
    private UserEntity user;

    
    (name = "fk_user_state")
    private UserStateEntity userState;

    @Column( "description")
    private String description;

    @Column( "init")
    private LocalDateTime init;

    @Column( "finish")
    private LocalDateTime finish;
}