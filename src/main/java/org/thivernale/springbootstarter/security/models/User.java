package org.thivernale.springbootstarter.security.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@SecondaryTable(name = "authorities", pkJoinColumns=@PrimaryKeyJoinColumn(name="username"))
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;
    @Id
    @PrimaryKeyJoinColumn()
    @Column(name = "username")
    private String userName;
    private String password;
    @Column(name = "enabled")
    private boolean active;
    @Column(table = "authorities", name = "authority")
    private String roles;

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
    public String getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }
}
