package com.proyect.mvp.infrastructure.exception;



import java.util.UUID;

public class PurchaseNotInPendingStateException extends RuntimeException {
    public PurchaseNotInPendingStateException(UUID id) {
        super("No se puede eliminar la compra con ID " + id + " porque su estado no es 'pendiente'.");
    }
}

