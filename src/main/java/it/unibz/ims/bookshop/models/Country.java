package it.unibz.ims.bookshop.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "country")
public class Country {

    @Id
    @Column (name = "countryid")
    private int countryId;
    
    @Column (name = "iso")
    private String iso;
    
    @Column (name = "name")
    private String name;
    
    @Column (name = "displayname")
    private String displayName;
    
    @Column (name = "phonecode")
    private int phonecode;

    protected Country() {}

    public int getCountryId() {
        return this.countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public int getPhonecode() {
        return this.phonecode;
    }

    public void setPhonecode(int phonecode) {
        this.phonecode = phonecode;
    }
}
