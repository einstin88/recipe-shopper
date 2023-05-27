-- Init db
DROP DATABASE IF EXISTS auth_server;

CREATE DATABASE auth_server;

USE auth_server;

-- Users table
DROP TABLE IF EXISTS users;

CREATE TABLE users(
    id VARCHAR(11),
    username VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(1028) NOT NULL,
    first_name VARCHAR(64),
    last_name VARCHAR(64),
    email VARCHAR(256) UNIQUE,
    timeStamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE INDEX users_username ON users(username);

-- authority types
DROP TABLE IF EXISTS authorities;

CREATE TABLE authorities(
    authority VARCHAR(64),
    PRIMARY KEY (authority)
);

-- User authorities table
DROP TABLE IF EXISTS user_authorities;

CREATE TABLE user_authorities(
    id int UNSIGNED AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL,
    authority VARCHAR(64),
    PRIMARY KEY (id),
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (authority) REFERENCES authorities(authority)
);