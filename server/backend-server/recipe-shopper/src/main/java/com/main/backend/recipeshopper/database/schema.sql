SHOW DATABASES;

-- DB creation
DROP DATABASE IF EXISTS recipe_shopper;

CREATE DATABASE recipe_shopper;

USE recipe_shopper;

-- Table: Categories
DROP TABLE IF EXISTS categories;

CREATE TABLE categories(
    name VARCHAR(20),
    PRIMARY KEY (name)
);

INSERT INTO categories (name)
VALUES ("vegetables"),
    ("meats"),
    ("dairies"),
    ("condiments");

-- Table: Products
DROP TABLE IF EXISTS products;

CREATE TABLE products(
    product_id VARCHAR(11),
    name VARCHAR(40) NOT NULL,
    url VARCHAR(200),
    pack_size VARCHAR(20),
    price DECIMAL(6, 2) NOT NULL,
    img VARCHAR(400),
    category VARCHAR(20) NOT NULL,
    PRIMARY KEY (product_id),
    CONSTRAINT FOREIGN KEY (category) REFERENCES categories(name)
);

-- Table: