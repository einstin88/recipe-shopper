package com.recipeshopper.jwtauthserver.database;

public class Queries {
    public static final String SQL_INSERT_USER = """
            INSERT INTO users(
                    id,
                    username,
                    password,
                    first_name,
                    last_name,
                    email
                )
            VALUES (?, ?, ?, ?, ?, ?);
                """;

    public static final String SQL_FIND_USER_USERNAME = """
            SELECT *
            FROM users
            WHERE username = ?;
                """;
}
