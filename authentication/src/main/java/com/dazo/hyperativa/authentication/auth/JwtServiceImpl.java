package com.dazo.hyperativa.authentication.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtProperty jwtProperty;

    @Override
    public String generateToken(String username, Collection<GrantedAuthority> authorities) {
        Map<String, Object> claims = new HashMap<>();

        if (!Objects.isNull(authorities)) {
            log.info("Authorities: {}", authorities);
            List<String> authoritiesString = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            claims.put("authorities", authoritiesString);
        }

        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {

        Instant now = Instant.now();
        Date expiration = Date.from(now.plus(30, ChronoUnit.MINUTES));

        log.info("Create token for username: {}", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(expiration)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperty.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
