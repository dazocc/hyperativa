package com.dazo.hyperativa.card.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final transient MessageEnum messageBusiness;
    private final transient Object[] parameters;

    public BusinessException(MessageEnum messageBusiness) {
        this(messageBusiness, null);
    }

    public BusinessException(MessageEnum messageBusiness, Object... parameters) {
        this.messageBusiness = messageBusiness;
        this.parameters = parameters;
    }
}
