-- Insert product into table
INSERT INTO products (
        product_id,
        name,
        url,
        pack_size,
        price,
        img,
        category
    )
VALUES (?, ?, ?, ?, ?, ?, ?);

-- Update product in table
UPDATE products
SET url = ?,
    price = ?,
    img = ?
WHERE name = ?
    AND pack_size = ?
    AND category = ?;

-- Query for product
SELECT *
FROM products
WHERE name = ?
    AND pack_size = ?
    AND category = ?;

-- Query for products
SELECT *
FROM products
LIMIT ? OFFSET ?
WHERE category = ?;

-- Query for recipes
SELECT *
FROM recipes
LIMIT ? OFFSET ?;

-- Query for recipe ingredients
SELECT pdt.product_id,
    pdt.name,
    pdt.url,
    pdt.pack_size,
    pdt.price,
    pdt.img,
    pdt.category,
    pdt.timeStamp,
    ri.quantity
FROM recipe_ingredients AS ri
    INNER JOIN products AS pdt ON ri.product_id = pdt.product_id
WHERE ri.recipe_id = ?;

-- Query for recipe by name and creator
SELECT *
FROM recipes
WHERE recipe_name = ?
    AND recipe_creator = ?;

-- Insert new recipe
INSERT INTO recipes (
        recipe_id,
        recipe_name,
        recipe_creator,
        procedures
    )
VALUES (?, ?, ?, ?);

-- Insert recipe ingredients
INSERT INTO recipe_ingredients (recipe_id, product_id, quantity)
VALUES (?, ?, ?);

-- Update recipe details
UPDATE recipes
SET recipe_name = ?,
    recipe_creator = ?,
    procedures = ?
WHERE recipe_id = ?;

-- Delete recipe ingredient
DELETE FROM recipe_ingredients
WHERE product_id = ?;