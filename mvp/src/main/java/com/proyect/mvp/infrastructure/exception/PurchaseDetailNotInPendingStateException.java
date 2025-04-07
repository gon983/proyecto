package com.proyect.mvp.infrastructure.exception;

import java.util.UUID;

public class PurchaseDetailNotInPendingStateException extends RuntimeException {
    public PurchaseDetailNotInPendingStateException(UUID id) {
        super("No se puede eliminar el detalle de compra con ID " + id + " porque su estado no es 'pendiente'.");
    }
}
