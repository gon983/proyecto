package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;


@Table( "salehistory")
@Getter
@NoArgsConstructor
public class SaleHistoryEntity {

    @Id
    
    @Column( "id_sale_history")
    private String idSaleHistory;

    
    (name = "fk_sale")
    private SaleEntity  sale;

    
    (name = "fk_sale_state")
    private SaleStateEntity  saleState;

    @Column( "description")
    private String description;

    @Column( "init")
    private LocalDateTime init;

    @Column( "finish")
    private LocalDateTime finish;
}