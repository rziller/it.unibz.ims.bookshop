package it.unibz.ims.bookshop.models;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name="customerid", updatable = false, nullable = false)
    private String customerId;

    @Column (name="isregistered")
    private boolean isRegistered;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="email")
    private String email;

    @Column(name="shippingaddressid")
    private UUID shippingAddressId;

    @Column(name="billingaddressid")
    private UUID billingAddressId;

    @Column(name="paymentmethodenumid")
    private int paymentMethodId;

    @Column(name="isadmin")
    private boolean isAdmin;

    @Transient
    private Address shippingAddress;

    @Transient
    private Address billingAddress;


    public Customer() {
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isRegistered() {
        return this.isRegistered;
    }

    public void setRegistered(boolean registered) {
        this.isRegistered = registered;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getShippingAddressId() {
        return this.shippingAddressId;
    }

    public void setShippingAddressId(UUID shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
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

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAdress) {
        this.shippingAddress = shippingAdress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public boolean isValid() {
        return !this.name.isEmpty() && !this.surname.isEmpty() && !this.email.isEmpty();
    }

    public boolean getIsAdmin() { return this.isAdmin;}

    public void setIsAdmin( boolean isAdmin)  {
        this.isAdmin = isAdmin;
    }


}
