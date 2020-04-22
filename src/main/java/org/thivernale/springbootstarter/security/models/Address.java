package org.thivernale.springbootstarter.security.models;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.javafaker.Faker;

@Embeddable
public class Address extends com.github.javafaker.Address {

    @Transient
    @Autowired
    protected Faker faker;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    protected Address(Faker faker) {
        super(faker);
        // TODO Auto-generated constructor stub
    }

    public String getStreet() {
        return streetAddress();
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city != null ? city : city();
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return stateAbbr();
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode();
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

}
