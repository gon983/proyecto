package com.proyect.mvp.application.services;

import java.time.OffsetDateTime;

public class SimuledPayment {
    private Long id;
    private String status;
    private OffsetDateTime dateApproved;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(OffsetDateTime dateApproved) {
        this.dateApproved = dateApproved;
    }
}
