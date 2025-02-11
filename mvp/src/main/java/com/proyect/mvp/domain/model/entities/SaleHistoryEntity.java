package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;

@Entity
@Table(name = "salehistory")
@Getter
@NoArgsConstructor
public class SaleHistoryEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_sale_history")
    private String idSaleHistory;

    @ManyToOne
    @JoinColumn(name = "fk_sale")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "fk_sale_state")
    private SaleState saleState;

    @Column(name = "description")
    private String description;

    @Column(name = "init")
    private LocalDateTime init;

    @Column(name = "finish")
    private LocalDateTime finish;
}