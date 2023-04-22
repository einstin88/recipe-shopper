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
            UPDATE products
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

    public static final String SQL_FIND_PRODUCTS = """
            SELECT *
            FROM products
            WHERE category = ?
            LIMIT ? OFFSET ?
                """;

    public static final String SQL_FIND_RECIPES = """
            SELECT *
            FROM recipes
            LIMIT ? OFFSET ?
                """;

    public static final String SQL_FIND_RECIPE_INGREDIENTS = """
            SELECT pdt.product_id,
                pdt.name,
                pdt.url,
                pdt.pack_size,
                pdt.price,
                pdt.img,
                pdt.category,
                pdt.timeStamp
            FROM recipe_ingredients AS ri
                INNER JOIN products AS pdt ON ri.product_id = pdt.product_id
            WHERE ri.recipe_id = ?
                """;

    public static final String SQL_FIND_RECIPE_NAME_CREATOR = """
            SELECT *
            FROM recipes
            WHERE recipe_name = ?
                AND recipe_creator = ?
                """;

    public static final String SQL_INSERT_RECIPE = """
            INSERT INTO recipes (
                    recipe_id,
                    recipe_name,
                    recipe_creator,
                    procedures
                )
            VALUES (?, ?, ?, ?)
                """;

    public static final String SQL_INSERT_RECIPE_INGREDIENT = """
            INSERT INTO recipe_ingredients (recipe_id, product_id, quantity)
            VALUES (?, ?, ?)
                """;

    public static final String SQL_UPDATE_RECIPE = """
            UPDATE recipes
            SET recipe_name = ?,
                recipe_creator = ?,
                procedures = ?
            WHERE recipe_id = ?;
                """;

    public static final String SQL_DEL_INGREDIENT = """
            DELETE FROM recipe_ingredients
            WHERE product_id = ?
                """;
}
