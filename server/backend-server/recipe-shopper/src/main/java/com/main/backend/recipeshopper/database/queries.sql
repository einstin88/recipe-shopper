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
UPDATE products (url, price, img)
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