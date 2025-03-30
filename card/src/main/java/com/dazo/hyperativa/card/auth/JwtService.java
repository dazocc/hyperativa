package com.dazo.hyperativa.card.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface JwtService {
    Jws<Claims> extractJws(String token);
    String extractUsername(Jws<Claims> jws);
    List<SimpleGrantedAuthority> extractAuthorities(Jws<Claims> jws);
}
