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

    @Transient
    private UserEntity user;

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

    @Column("id_location")
    private UUID idLocation;
    @Transient
    private Location location;
    @Column("fk_recorrido")
    UUID fkRecorrido;
   

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

    public void calculateAndSetAmount() {
        if (details != null) {
            this.amount = details.stream()
                .mapToDouble(detail -> detail.getUnitPrice() * detail.getQuantity())
                .sum();
        } else {
            this.amount = 0;
        }
    }
    

    public void setPaymentDate(OffsetDateTime paymentDate){
        this.mpPaymentDate = paymentDate;
    }

    public void setUser(UserEntity user){
        this.user = user;
    }
    public void setLocation(Location location){
        this.location = location;
    }


    


}