package com.tycorp.eb.spring_security.jwt_auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String jwtToken;
    public String getJwtToken() {
        return jwtToken;
    }

    public JwtAuthenticationToken(String jwtToken) {
        super(null);
        this.jwtToken = jwtToken;
    }

    @Override
    public Object getCredentials() { return null; }
    @Override
    public Object getDetails() { return null; }
    @Override
    public Object getPrincipal() { return null; }
    @Override
    public String getName() { return null; }

    @Override
    public boolean isAuthenticated() { return false; }
    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException { }

}
