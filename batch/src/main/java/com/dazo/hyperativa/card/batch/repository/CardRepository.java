package com.dazo.hyperativa.card.batch.repository;

public interface CardRepository {
    boolean verifyExistsCardByNumber(String number);
    void insertCard(String number);
    void deleteAll();
}
