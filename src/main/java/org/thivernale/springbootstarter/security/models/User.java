package org.thivernale.springbootstarter.security.models;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity()
@Table(name = "users")
@SecondaryTable(name = "authorities", pkJoinColumns = @PrimaryKeyJoinColumn(name="username"))
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;
    @Id
    @PrimaryKeyJoinColumn()
    @Column(name = "username")
    // without additional properties it is equal to not specifying a mapping annotation
    @Basic()
    private String userName;
    private String password;
    @Column(name = "enabled")
    private boolean active;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date added;
    @Column(table = "authorities", name = "authority")
    @Lob
    private String roles;
    // field is not persistent (it will be ignored by Hibernate)
    @Transient
    private String nickname;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "street_name")),
        @AttributeOverride(name = "city", column = @Column(name = "city_name")),
        @AttributeOverride(name = "state", column = @Column(name = "state_code")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "home_zip_code")),
    })
    Address address;
    @Embedded
    Address officeAddress;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public Date getAdded() {
        return added;
    }
    public void setAdded(Date added) {
        if (added == null) {
            added = new Date();
        }
        this.added = added;
    }
    public String getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }
    public String getNickname() {
        return nickname != null ? nickname : userName;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
}
