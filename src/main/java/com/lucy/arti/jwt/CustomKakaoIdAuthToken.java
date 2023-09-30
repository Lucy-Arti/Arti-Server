package com.lucy.arti.jwt;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomKakaoIdAuthToken extends AbstractAuthenticationToken {
    private final Object principal;
    private Object credentials;

    public CustomKakaoIdAuthToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public CustomKakaoIdAuthToken(Object principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorites) {
        super(authorites);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);

    }
    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
