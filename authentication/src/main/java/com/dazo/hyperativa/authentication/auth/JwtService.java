package com.dazo.hyperativa.authentication.auth;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtService {
    String generateToken(String username, Collection<GrantedAuthority> authorities);
}
