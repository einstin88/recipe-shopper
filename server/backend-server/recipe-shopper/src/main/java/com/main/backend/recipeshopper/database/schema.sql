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
VALUES ("baking"),
    ("butter"),
    ("cheese"),
    ("condiments"),
    ("eggs"),
    ("fishcakes"),
    ("flour"),
    ("fruits"),
    ("meats-seafood"),
    ("milk"),
    ("noodles"),
    ("nuts-seeds"),
    ("pasta"),
    ("rice"),
    ("sake-soju"),
    ("spice-paste"),
    ("tofu"),
    ("toppings"),
    ("vegetables"),
    ("wine");

;

-- Table: Products
DROP TABLE IF EXISTS products;

CREATE TABLE products(
    product_id VARCHAR(11),
    name VARCHAR(64) NOT NULL,
    url VARCHAR(200),
    pack_size VARCHAR(20),
    price DECIMAL(6, 2) NOT NULL,
    img VARCHAR(400),
    category VARCHAR(20) NOT NULL,
    timeStamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id),
    CONSTRAINT fk_category FOREIGN KEY (category) REFERENCES categories(name)
);

-- Table: Recipes
DROP TABLE IF EXISTS recipes;

CREATE TABLE recipes(
    recipe_id VARCHAR(11) NOT NULL,
    recipe_name VARCHAR(40) NOT NULL,
    recipe_creator VARCHAR(40) NULL,
    procedures text NOT NULL,
    timeStamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (recipe_id)
);

-- Intermediate Table: recipe x ingredients 
DROP TABLE IF EXISTS recipe_ingredients;

CREATE TABLE recipe_ingredients(
    id INT UNSIGNED AUTO_INCREMENT,
    recipe_id VARCHAR(11) NULL,
    product_id VARCHAR(11) NOT NULL,
    quantity smallint UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT
);

-- Table: Cart
-- DROP TABLE IF EXISTS carts;
-- CREATE TABLE carts(
--     cart_id VARCHAR(11) NOT NULL,
--     username VARCHAR(40) NOT NULL,
--     total DECIMAL(6, 2) NOT NULL,
--     PRIMARY KEY(cart_id)
-- );
-- -- Intermediate:
-- DROP TABLE IF EXISTS cart_items;
-- CREATE TABLE cart_items(
--     id INT UNSIGNED AUTO_INCREMENT,
--     cart_id VARCHAR(11) NULL,
--     recipe_id VARCHAR(11) NULL,
--     PRIMARY KEY(id),
--     FOREIGN KEY (cart_id) REFERENCES carts(cart_id) ON DELETE
--     SET NULL,
--         FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id) ON DELETE
--     SET NULL
-- );