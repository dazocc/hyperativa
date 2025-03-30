package com.dazo.hyperativa.card.auth;

import com.dazo.hyperativa.card.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.util.List;

import static com.dazo.hyperativa.card.exception.MessageEnum.TOKEN_INVALID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtProperty jwtProperty;

    @Override
    public Jws<Claims> extractJws(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            log.error("Token is invalid", e);
            throw new BusinessException(TOKEN_INVALID);
        }
    }

    @Override
    public String extractUsername(Jws<Claims> jws) {
        Claims claims = jws.getBody();
        return claims.getSubject();
    }

    @Override
    public List<SimpleGrantedAuthority> extractAuthorities(Jws<Claims> jws) {

        log.info("Retrive user authorities: {}", jws.getBody());
        List<String> authorities = jws.getBody().get("authorities", List.class);

        if (!ObjectUtils.isEmpty(authorities)) {

            return authorities.stream()
                    .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority))
                    .toList();
        }

        return null;
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperty.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
