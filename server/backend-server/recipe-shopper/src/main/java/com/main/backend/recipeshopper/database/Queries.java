package com.main.backend.recipeshopper.database;

public class Queries {
    public static final String SQL_INSERT_PRODUCT = """
            INSERT INTO products (
                product_id,
                name,
                url,
                pack_size,
                price,
                img,
                category
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

    public static final String SQL_UPDATE_PRODUCT = """
            UPDATE products (url, price, img)
            SET url = ?,
                price = ?,
                img = ?
            WHERE name = ?
                AND pack_size = ?
                AND category = ?
                    """;

    public static final String SQL_FIND_PRODUCT = """
            SELECT *
            FROM products
            WHERE name = ?
                AND pack_size = ?
                AND category = ?
                    """;

}
