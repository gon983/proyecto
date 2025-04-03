package com.proyect.mvp.domain.model.entities;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("purchase")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseEntity {

    @Id
    @Column("id_purchase")
    private UUID idPurchase;

    @Column("fk_user")
    private UUID fkUser;

    @Column("amount")
    private double amount;



    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;


    @Transient
    private List<PurchaseDetailEntity> details;

    @Column("fk_current_state")
    private UUID fkCurrentState;

    @Column("mp_preference_id")
    private String mpPreferenceId;

    @Column("mp_payment_id")
    private String mpPaymentId;

    @Column("mp_payment_date")
    private OffsetDateTime mpPaymentDate;

    @Column("external_reference")
    private String externalReference;
   

    public void addDetails(List<PurchaseDetailEntity> details){
        this.details = details;
    }

    public void setMpPreferenceId(String mpPreferenceId){
        this.mpPreferenceId = mpPreferenceId;
    }
    public void setExternalReference(String externalReference){
        this.externalReference = externalReference;

    }

    public void setMpPaymentId(String mpPaymentId){
        this.mpPaymentId = mpPaymentId;

    }

    public void setFkCurrentState(UUID state){
        this.fkCurrentState = state;

    }

    public void setPaymentDate(OffsetDateTime paymentDate){
        this.mpPaymentDate = paymentDate;
    }


    


}