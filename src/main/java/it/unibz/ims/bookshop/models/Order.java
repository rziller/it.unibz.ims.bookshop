package it.unibz.ims.bookshop.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "custorder")
public class Order {

    @Id
    @Column(name="custorderid", updatable = false, nullable = false)
    private UUID orderId;

    @Column(name="customerid")
    private String customerId;

    @Column(name="shippingaddressid")
    private UUID shipingAddressId;

    @Column(name="billingaddressid")
    private UUID billingAddressId;

    @Column(name="paymentmethodenumid")
    private int paymentMethodId;

    @Column(name="orderstatusenumid", nullable = false)
    private int orderStatusId;

    @Column(name="orderdate")
    private LocalDateTime orderDate;

    @Transient
    private List<Orderline> orderLines;

    public Order() {
        this.orderId = UUID.randomUUID();
        this.orderStatusId = 0;
        this.orderLines = new ArrayList<>();
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public UUID getShipingAddressId() {
        return this.shipingAddressId;
    }

    public void setShipingAddressId(UUID shipingAddressId) {
        this.shipingAddressId = shipingAddressId;
    }

    public UUID getBillingAddressId() {
        return this.billingAddressId;
    }

    public void setBillingAddressId(UUID billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public int getPaymentMethodId() {
        return this.paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public int getOrderStatusId() {
        return this.orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public LocalDateTime getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<Orderline> getOrderlines() {
        return this.orderLines;
    }

    public void setOrderlines(List<Orderline> orderlines) {
        this.orderLines = orderlines;
    }

    public List<Orderline> getOrderLines() {
        return this.orderLines;
    }

    public void setOrderLines(List<Orderline> orderLines) {
        this.orderLines = orderLines;
    }

    public int getTotalOrderlines() {
        return this.orderLines.size();
    }

    public float getTotalPrice() {
        float totalPrice = 0;

        for ( Orderline orderline: this.orderLines ) {
            totalPrice += orderline.getTotalPrice();
        }

        return totalPrice;
    }

    public float getTotalPriceTransient() {
        float totalPriceTransient = 0;

        for ( Orderline orderline: this.orderLines ) {
            totalPriceTransient += orderline.getTotalPriceTransient();
        }

        return totalPriceTransient;
    }

    public void freeze() {
        this.orderDate = LocalDateTime.now();
        this.orderStatusId = 1;

        for (Orderline orderline: this.orderLines) {
            orderline.setPrice( orderline.getTotalPriceTransient() );
        }
    }
}
