package com.dazo.hyperativa.card.advice;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MensageExceptionResponse {

    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();

    public MensageExceptionResponse(String message) {
        this.message = message;
    }
}
