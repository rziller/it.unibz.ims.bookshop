package it.unibz.ims.bookshop.models;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "custorderline")
public class Orderline {

    @Id
    @Column(name="custorderlineid", updatable = false, nullable = false)
    private UUID orderlineId;

    @Column(name="custorderid", updatable = false, nullable = false)
    private UUID orderId;

    @Column(name="productid", updatable = false, nullable = false)
    private UUID productId;

    @Column(name="qty", nullable = false)
    private int qty;

    @Column(name="price")
    private float price;

    @Transient
    private Product associatedProduct;

    public Orderline() {
        this.setOrderlineId( UUID.randomUUID() );
    }

    public UUID getOrderlineId() {
        return this.orderlineId;
    }

    public void setOrderlineId(UUID orderlineId) {
        this.orderlineId = orderlineId;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getProductId() {
        return this.productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQty() {
        return this.qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Product getAssociatedProduct() {
        return this.associatedProduct;
    }

    public void setAssociatedProduct(Product associatedProduct) {
        this.associatedProduct = associatedProduct;
    }

    public float getTotalPrice() {
        return this.getPrice();
    }

    public float getTotalPriceTransient() {
        return this.qty * this.associatedProduct.getPrice();
    }

    public Orderline update(Orderline orderline) {
        this.setQty( orderline.qty );
        return this;
    }
}
