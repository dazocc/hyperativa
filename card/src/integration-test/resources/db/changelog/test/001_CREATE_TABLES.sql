--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;

CREATE TABLE cards (
    id INT PRIMARY KEY AUTO_INCREMENT ,
    number VARCHAR(18) NOT NULL
);

CREATE UNIQUE INDEX ix_cards_number ON cards(number);

CREATE TABLE files (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(10) NOT NULL,
    data BLOB not null,
    data_error BLOB
);
