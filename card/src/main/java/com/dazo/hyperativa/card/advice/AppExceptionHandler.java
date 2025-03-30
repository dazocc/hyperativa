package com.dazo.hyperativa.card.advice;

import com.dazo.hyperativa.card.exception.BusinessException;
import com.dazo.hyperativa.card.exception.MessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.dazo.hyperativa.card.exception.MessageEnum.ACCESS_DENIED;
import static com.dazo.hyperativa.card.exception.MessageEnum.ERROR_500;

@RestControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<MensageExceptionResponse> businessException(BusinessException businessException){

        String mensagem = getMensagem(businessException);

        MensageExceptionResponse mensageExceptionResponse = new MensageExceptionResponse(mensagem);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensageExceptionResponse);
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<MensageExceptionResponse> businessException(AuthorizationDeniedException exception){

        String mensagem = getMensagem(ACCESS_DENIED);

        MensageExceptionResponse mensageExceptionResponse = new MensageExceptionResponse(mensagem);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(mensageExceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensageExceptionResponse> businessException(Exception exception){

        String mensagem = getMensagem(ERROR_500);

        MensageExceptionResponse mensageExceptionResponse = new MensageExceptionResponse(mensagem);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensageExceptionResponse);
    }


    private String getMensagem(BusinessException businessException) {
        return messageSource.getMessage(businessException.getMessageBusiness().toString(),
                businessException.getParameters(), LocaleContextHolder.getLocale());
    }

    private String getMensagem(MessageEnum mensagem) {
        return messageSource.getMessage(mensagem.toString(), null, LocaleContextHolder.getLocale());
    }

}
