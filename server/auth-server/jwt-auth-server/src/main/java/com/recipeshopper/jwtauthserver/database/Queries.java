package com.recipeshopper.jwtauthserver.database;

public class Queries {
    public static final String SQL_INSERT_USER = """
            INSERT INTO auth_server.users(
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
            FROM auth_server.users
            WHERE username = ?;
                """;

    public static final String SQL_FIND_USER_AUTHORITIES = """
            SELECT *
            FROM auth_server.user_authorities
            WHERE username = ?;
                """;
}
