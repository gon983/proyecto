package com.proyect.mvp.application.dtos.requests;

import java.util.List;

import com.proyect.mvp.application.dtos.create.PurchaseCreateDTO;
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;

// Helper class to encapsulate PurchaseCreateDTO and List<PurchaseDetailEntity>
    public class PurchaseCreationRequest {
        private PurchaseCreateDTO purchaseDto;
        private List<PurchaseDetailEntity> details;

        public PurchaseCreateDTO getPurchaseDto() {
            return purchaseDto;
        }

        public void setPurchaseDto(PurchaseCreateDTO purchaseDto) {
            this.purchaseDto = purchaseDto;
        }

        public List<PurchaseDetailEntity> getDetails() {
            return details;
        }

        public void setDetails(List<PurchaseDetailEntity> details) {
            this.details = details;
        }
    }

