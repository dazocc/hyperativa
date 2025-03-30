package com.dazo.hyperativa.card.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    Optional<CardEntity> findCardByNumber(String number);
    boolean existsByNumber(String cardNumber);
}
