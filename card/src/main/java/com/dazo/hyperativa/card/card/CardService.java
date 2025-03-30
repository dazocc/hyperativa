package com.dazo.hyperativa.card.card;

import com.dazo.hyperativa.card.card.dto.CreateCardRequest;

public interface CardService {
    Long findCardByNumber(String number);
    void createCard(CreateCardRequest createCardRequest);
}
