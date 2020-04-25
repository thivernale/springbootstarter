package org.thivernale.springbootstarter.security.models;

import javax.persistence.Embeddable;

@Embeddable
public class UserRole {
    private String authority;

    public UserRole() {
    }

    public UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
