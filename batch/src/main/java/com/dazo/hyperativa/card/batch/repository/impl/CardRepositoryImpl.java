package com.dazo.hyperativa.card.batch.repository.impl;

import com.dazo.hyperativa.card.batch.repository.CardRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CardRepositoryImpl implements CardRepository {

    public static final String DELETE_ALL_CARDS = "delete from cards";
    private final JdbcTemplate jdbcTemplate;

    public static final String EXISTS_CARDS_BY_NUMBER = "select 1 from cards where number = ?";
    public static final String INSERT_CARDS_NUMBER = "INSERT INTO cards (number) VALUES (?)";

    public CardRepositoryImpl(@Qualifier("appJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean verifyExistsCardByNumber(String number) {

        try {
            Integer exists = jdbcTemplate.queryForObject(EXISTS_CARDS_BY_NUMBER, Integer.class, number);
            return exists != null && exists == 1;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public void insertCard(String number) {
        jdbcTemplate.update(INSERT_CARDS_NUMBER, number);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_CARDS);
    }
}
