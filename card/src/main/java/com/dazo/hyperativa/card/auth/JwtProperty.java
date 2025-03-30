package com.dazo.hyperativa.card.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {

    private String secret;
}
