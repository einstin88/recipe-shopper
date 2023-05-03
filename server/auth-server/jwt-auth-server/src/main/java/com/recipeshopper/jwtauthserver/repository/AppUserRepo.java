package com.recipeshopper.jwtauthserver.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import static com.recipeshopper.jwtauthserver.database.Queries.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.recipeshopper.jwtauthserver.model.AppUser;

@Repository
public class AppUserRepo {
    private class UserRowMapper implements RowMapper<AppUser> {
        @Override
        @Nullable
        public AppUser mapRow(ResultSet arg0, int arg1) throws SQLException {
            return null;
        }
    }

    @Autowired
    private JdbcTemplate template;

    public Boolean insertUser(AppUser user) {
        return template.update(SQL_INSERT_USER,
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()) == 1;
    }

    public AppUser findUserByUsername(String username) {
        return template.queryForObject(SQL_FIND_USER_USERNAME,
                new UserRowMapper(),
                username);
    }
}
