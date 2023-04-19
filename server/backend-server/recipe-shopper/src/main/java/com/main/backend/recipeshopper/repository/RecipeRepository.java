package com.main.backend.recipeshopper.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.main.backend.recipeshopper.model.Recipe;
import static com.main.backend.recipeshopper.database.Queries.*;

@Repository
public class RecipeRepository {
    @Autowired
    private JdbcTemplate template;

    public List<Recipe> findRecipes(Integer offset, Integer limit) {

        return template.query(
                SQL_FIND_RECIPES,
                DataClassRowMapper.newInstance(Recipe.class),
                limit, offset);
    }
}
