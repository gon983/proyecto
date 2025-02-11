package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;

@Entity
@Table(name = "userhistory")
@Getter
@NoArgsConstructor
public class UserHistoryEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_user_history")
    private String idUserHistory;

    @ManyToOne
    @JoinColumn(name = "fk_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_user_state")
    private UserState userState;

    @Column(name = "description")
    private String description;

    @Column(name = "init")
    private LocalDateTime init;

    @Column(name = "finish")
    private LocalDateTime finish;
}