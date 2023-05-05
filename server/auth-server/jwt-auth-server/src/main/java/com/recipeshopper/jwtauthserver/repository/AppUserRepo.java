package com.recipeshopper.jwtauthserver.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import static com.recipeshopper.jwtauthserver.database.Queries.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.recipeshopper.jwtauthserver.model.AppUser;

@Repository
public class AppUserRepo {
    @Autowired
    private JdbcTemplate template;

    private class UserRowMapper implements RowMapper<AppUser> {
        @Override
        @Nullable
        public AppUser mapRow(ResultSet rs, int row) throws SQLException {
            String username = rs.getString("username");
            return new AppUser(
                    rs.getString("id"),
                    username,
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    new LinkedList<>());
                    // findUserAuthorities(username));
        }
    }

    public Boolean insertUser(AppUser user) {
        try {
            return template.update(SQL_INSERT_USER,
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail()) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public Optional<AppUser> findUserByUsername(String username) {
        try {
            return Optional.of(
                    template.queryForObject(SQL_FIND_USER_USERNAME,
                            new UserRowMapper(),
                            username));

        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<? extends GrantedAuthority> findUserAuthorities(String username) {
        return template.queryForStream(SQL_FIND_USER_AUTHORITIES,
                SingleColumnRowMapper.newInstance(String.class),
                username)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
