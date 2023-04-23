package com.main.backend.recipeshopper.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.main.backend.recipeshopper.model.Ingredient;
import com.main.backend.recipeshopper.model.Recipe;
import static com.main.backend.recipeshopper.database.Queries.*;

@Repository
public class RecipeRepository {
    private class RecipeRowMapper implements RowMapper<Recipe<Ingredient>> {
        @Override
        @Nullable
        public Recipe<Ingredient> mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Recipe<>(
                    rs.getString("recipeId"),
                    rs.getString("recipeName"),
                    rs.getString("recipeCreator"),
                    rs.getString("procedures"),
                    findRecipeIngredients(rs.getString("recipeId")).toList(),
                    rs.getTimestamp("timeStamp").toLocalDateTime());
        }
    }

    @Autowired
    private JdbcTemplate template;

    public List<Recipe<Ingredient>> findRecipes(Integer offset, Integer limit) {
        return template.query(
                SQL_FIND_RECIPES,
                new RecipeRowMapper(),
                limit, offset);
    }

    public Optional<Recipe<Ingredient>> findRecipeByNameCreator(String name, String creator) {
        try {
            return Optional.of(
                    template.queryForObject(
                            SQL_FIND_RECIPE_NAME_CREATOR,
                            new RecipeRowMapper(),
                            name, creator));

        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Boolean insertRecipe(Recipe<Ingredient> recipe) {
        return template.update(
                SQL_INSERT_RECIPE,
                recipe.recipeId(),
                recipe.recipeName(),
                recipe.recipeCreator(),
                recipe.procedures()) == 1;
    }

    public Boolean insertRecipeIngredient(String recipeId, List<Ingredient> products) {
        int[] results = template.batchUpdate(
                SQL_INSERT_RECIPE_INGREDIENT,
                new BatchPreparedStatementSetter() {
                    @Override
                    public int getBatchSize() {
                        return products.size();
                    }

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, recipeId);
                        ps.setString(2, products.get(i).getProductId());
                        ps.setInt(3, products.get(i).getQuantity());
                    }
                });

        for (int result : results) {
            if (result != 1)
                return false;
        }
        return true;
    }

    public Stream<Ingredient> findRecipeIngredients(String recipeId) {
        return template.queryForStream(
                SQL_FIND_RECIPE_INGREDIENTS,
                DataClassRowMapper.newInstance(Ingredient.class),
                recipeId);
    }

    public Boolean updateRecipe(Recipe<Ingredient> recipe) {
        return template.update(
                SQL_UPDATE_RECIPE,
                recipe.recipeName(),
                recipe.recipeCreator(),
                recipe.recipeId(),
                recipe.procedures()) == 1;
    }

    public Boolean deleteIngredient(String productId) {
        return template.update(
                SQL_DEL_INGREDIENT,
                productId) == 1;
    }
}
