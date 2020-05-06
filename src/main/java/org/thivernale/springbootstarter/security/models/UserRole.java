package org.thivernale.springbootstarter.security.models;

import javax.persistence.Cacheable;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Embeddable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
