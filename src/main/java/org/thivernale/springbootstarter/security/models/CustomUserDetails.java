package org.thivernale.springbootstarter.security.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private String userName;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;

    public CustomUserDetails() {
    }

    public CustomUserDetails(String userName) {
        this.userName = userName;
    }

    public CustomUserDetails(User user) {
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.authorities = /*new ArrayList<GrantedAuthority>();
        Arrays.asList(user.getRoles().split(",")).forEach(role -> this.authorities.add(new SimpleGrantedAuthority(role)));*/
            Arrays.stream(user.getRoles().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
