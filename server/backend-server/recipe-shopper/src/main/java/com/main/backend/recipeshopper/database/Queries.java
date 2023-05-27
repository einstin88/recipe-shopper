package com.main.backend.recipeshopper.database;

public class Queries {
    public static final String SQL_INSERT_PRODUCT = """
            INSERT INTO recipe_shopper.products (
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
            UPDATE recipe_shopper.products
            SET url = ?,
                price = ?,
                img = ?
            WHERE name = ?
                AND pack_size = ?
                AND category = ?
                    """;

    public static final String SQL_FIND_PRODUCT = """
            SELECT *
            FROM recipe_shopper.products
            WHERE name = ?
                AND pack_size = ?
                AND category = ?
                    """;

    public static final String SQL_FIND_PRODUCTS = """
            SELECT *
            FROM recipe_shopper.products
            WHERE category = ?
                """;

    public static final String SQL_FIND_RECIPES = """
            SELECT *
            FROM recipe_shopper.recipes
                """;

    public static final String SQL_FIND_USER_RECIPES = """
            SELECT *
            FROM recipes
            WHERE recipe_creator = ?
                """;

    public static final String SQL_FIND_RECIPE_INGREDIENTS = """
            SELECT pdt.product_id,
                pdt.name,
                pdt.url,
                pdt.pack_size,
                pdt.price,
                pdt.img,
                pdt.category,
                pdt.timeStamp,
                ri.quantity
            FROM recipe_shopper.recipe_ingredients AS ri
                INNER JOIN recipe_shopper.products AS pdt ON ri.product_id = pdt.product_id
            WHERE ri.recipe_id = ?;
                """;

    public static final String SQL_FIND_RECIPE_NAME_CREATOR = """
            SELECT *
            FROM recipe_shopper.recipes
            WHERE recipe_name = ?
                AND recipe_creator = ?
                """;

    public static final String SQL_FIND_RECIPE_ID = """
            SELECT *
            FROM recipe_shopper.recipes
            WHERE recipe_id = ?;
                """;

    public static final String SQL_INSERT_RECIPE = """
            INSERT INTO recipe_shopper.recipes (
                    recipe_id,
                    recipe_name,
                    recipe_creator,
                    procedures
                )
            VALUES (?, ?, ?, ?)
                """;

    public static final String SQL_INSERT_RECIPE_INGREDIENT = """
            INSERT INTO recipe_shopper.recipe_ingredients (recipe_id, product_id, quantity)
            VALUES (?, ?, ?)
                """;

    public static final String SQL_UPDATE_RECIPE = """
            UPDATE recipe_shopper.recipes
            SET recipe_name = ?,
                recipe_creator = ?,
                procedures = ?
            WHERE recipe_id = ?;
                """;

    public static final String SQL_DEL_INGREDIENT = """
            DELETE FROM recipe_shopper.recipe_ingredients
            WHERE product_id = ?
                """;
}
