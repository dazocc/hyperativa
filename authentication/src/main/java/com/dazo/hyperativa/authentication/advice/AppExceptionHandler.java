package com.dazo.hyperativa.authentication.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

    public static final String USERNAME_OR_PASSWORD_INCORRECT = "Username or password incorrect";

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<MensageExceptionResponse> badCredentialsException(InsufficientAuthenticationException insufficientAuthenticationException){

        MensageExceptionResponse mensageExceptionResponse = new MensageExceptionResponse(USERNAME_OR_PASSWORD_INCORRECT);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensageExceptionResponse);
    }

}
