--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;

CREATE TABLE IF NOT EXISTS cards (
    id INT PRIMARY KEY AUTO_INCREMENT ,
    number VARCHAR(18) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS  ix_cards_number ON cards(number);

CREATE TABLE IF NOT EXISTS files (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(10) NOT NULL,
    data BLOB not null,
    data_error BLOB
);
