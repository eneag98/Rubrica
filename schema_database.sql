CREATE DATABASE IF NOT EXISTS informazioni;

USE informazioni;

CREATE TABLE IF NOT EXISTS person (
    id INTEGER PRIMARY KEY auto_increment,
    first VARCHAR(50)  NOT NULL,
    last VARCHAR(50) NOT NULL,
    address VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    age INTEGER NOT NULL
);

INSERT INTO Person (first, last, address, phone, age) VALUES
    ('Steve', 'Jobs', 'via Cupertino 13', '00612344', 56),
    ('Bill', 'Gates', 'via Redmond 10', '06688989', 60),
    ('Babbo', 'Natale', 'via del Polo Nord 1', '00000111', 99),
    ('Alberto', 'Guarini', 'via Avogadro 9', '01010101', 25),
    ('Mario', 'Rossi', 'via Roma 2', '01234567', 32),
    ('Maria', 'Rosso', 'via Torino 3', '11221122', 27),
    ('Giuseppe', 'De Stefanis', 'corso Morandi 4', '12341234', 33),
    ('Marco', 'Lello', 'via Bologna 5', '00112200', 40),
    ('Carlotta', 'Verdi', 'corso Orbassano 123', '01230123', 22),
    ('Giorgio', 'Giorgi', 'via Giorgio 222', '76543210', 21),
    ('Dario', 'Delillo', 'via Risorgimento 23', '18712410', 55),
    ('Paolo', 'Bonolis', 'via Barcellona 32', '65124812', 62);


CREATE TABLE IF NOT EXISTS user (
    id INTEGER PRIMARY KEY auto_increment,
    first VARCHAR(50) NOT NULL,
    last VARCHAR(50),
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    admin INTEGER  NOT NULL
);

INSERT INTO User (first, last, username, password, admin) VALUES
    ('Admin', null, 'admin', 'pwdAdmin24!', 1),
    ('Turing', null, 'turing', 'pwdTuring24!', 1),
    ('Mario', 'Rossi', 'mrossi', 'pwdMario24!', 0),
    ('Debora', 'Verdi', 'dverdi', 'pwdDebora24!', 0);