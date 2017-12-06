package com.toptal.demo.security.identity;


import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthentication implements Authentication {
    private final TokenUser driver;
    private boolean authenticated = true;

    public UserAuthentication(TokenUser user) {
        this.driver = user;
    }

    @Override
    public String getName() {
        return driver.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return driver.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return driver.getPassword();
    }

    @Override
    public TokenUser getDetails() {
        return driver;
    }

    @Override
    public Object getPrincipal() {
        return driver.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
