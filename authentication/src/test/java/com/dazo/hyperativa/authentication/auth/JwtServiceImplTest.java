package com.dazo.hyperativa.authentication.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class JwtServiceImplTest {

    @Mock
    private JwtProperty jwtProperty;

    @InjectMocks
    private JwtServiceImpl jwtService;

    JwtServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Must generate token with success")
    void mustGenerateToken() {
        when(jwtProperty.getSecret()).thenReturn("1ee8fb736f8a90c0f263c7525138d3d438f1a2de7692347a641cf904366d4161");

        String token = jwtService.generateToken("davison", null);
        assertNotNull(token);
    }

    @Test
    @DisplayName("Must generate token when inform authorities with success")
    void shouldGenerateTokenWithAuthorities() {
        when(jwtProperty.getSecret()).thenReturn("1ee8fb736f8a90c0f263c7525138d3d438f1a2de7692347a641cf904366d4161");

        String token = jwtService.generateToken("davison", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        assertNotNull(token);
    }

}