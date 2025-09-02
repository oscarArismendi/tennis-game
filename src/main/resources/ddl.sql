DROP DATABASE IF EXISTS tennis;
CREATE DATABASE tennis;
USE tennis;

CREATE TABLE player (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE
);

CREATE TABLE game (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    server_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    server_score INT DEFAULT 0,
    receiver_score INT DEFAULT 0,
    advantage ENUM('AD_IN', 'AD_OUT', 'NONE') DEFAULT 'NONE',
    state ENUM('NOT_STARTED', 'IN_PROGRESS', 'ENDED') DEFAULT 'NOT_STARTED',
    FOREIGN KEY (server_id) REFERENCES Player(id),
    FOREIGN KEY (receiver_id) REFERENCES Player(id),
    CONSTRAINT uq_server_receiver UNIQUE (server_id, receiver_id)
);