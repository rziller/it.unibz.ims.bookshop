package it.unibz.ims.bookshop.models;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @Column(name="addressid", updatable = false, nullable = false)
    private UUID addressId;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="street")
    private String street;

    @Column(name="zipcode")
    private String zipCode;

    @Column(name="city")
    private String city;

    @Column(name="countryid")
    private Integer countryId;

    @Transient
    private Product associatedCountry;

    public Address() {
        this.addressId = UUID.randomUUID();
    }

    public UUID getAddressId() {
        return this.addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
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

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCountryId() {
        return this.countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Product getAssociatedCountry() {
        return this.associatedCountry;
    }

    public void setAssociatedCountry(Product associatedCountry) {
        this.associatedCountry = associatedCountry;
    }

    public boolean isValid() {
        return  !this.name.isEmpty() && !this.surname.isEmpty() && !this.street.isEmpty() &&
                !this.zipCode.isEmpty() && !this.city.isEmpty() && !this.countryId.toString().isEmpty() &&
                !this.countryId.toString().isEmpty();
    }

}
