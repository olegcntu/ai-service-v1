package org.example.aiservicev3.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String principal;

    public ApiKeyAuthenticationToken(String principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}