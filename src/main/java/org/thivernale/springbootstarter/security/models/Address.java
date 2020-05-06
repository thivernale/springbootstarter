package org.thivernale.springbootstarter.security.models;

import javax.persistence.Cacheable;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.javafaker.Faker;

@Embeddable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Address extends com.github.javafaker.Address implements Cloneable {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    @Transient
    private User user;

    @Transient
    @Autowired
    public Faker getFaker() {
        return null;
    }

    public Address() {
        super(null);
    }

    public Address(Faker faker) {
        super(faker);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street != null ? street : streetAddressNumber() + " " + streetAddress();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city != null ? city : city();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state != null ? state : stateAbbr();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode != null ? zipCode : zipCode();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Address clone() throws CloneNotSupportedException {
        return (Address)super.clone();
    }
}
