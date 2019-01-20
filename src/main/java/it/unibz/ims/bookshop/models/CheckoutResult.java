package it.unibz.ims.bookshop.models;

import java.util.UUID;

public class CheckoutResult {

    private boolean isSuccess;
    private UUID orderId;

    public CheckoutResult() {}

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
