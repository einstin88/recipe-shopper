package com.main.backend.recipeshopper.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.main.backend.recipeshopper.model.Product;
import com.main.backend.recipeshopper.model.Recipe;
import static com.main.backend.recipeshopper.database.Queries.*;

@Repository
public class RecipeRepository {
    @Autowired
    private JdbcTemplate template;

    public Stream<Recipe> findRecipes(Integer offset, Integer limit) {
        return template.queryForStream(
                SQL_FIND_RECIPES,
                DataClassRowMapper.newInstance(Recipe.class),
                limit, offset);
    }

    public Optional<Recipe> findRecipeByNameCreator(String name, String creator) {
        try {
            return Optional.of(
                    template.queryForObject(
                            SQL_FIND_RECIPE_NAME_CREATOR,
                            DataClassRowMapper.newInstance(Recipe.class),
                            name, creator));

        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Boolean insertRecipe(Recipe recipe) {
        return template.update(
                SQL_INSERT_RECIPE,
                recipe.recipeId(),
                recipe.recipeName(),
                recipe.recipeCreator()) == 1;
    }

    public Boolean insertRecipeIngredient(String recipeId, List<String> products) {
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
                        ps.setString(2, products.get(i));
                    }
                });

        for (int result : results) {
            if (result != 1)
                return false;
        }
        return true;
    }

    public Stream<Product> findRecipeIngredients(String recipeId) {
        return template.queryForStream(
                SQL_FIND_RECIPE_INGREDIENTS,
                DataClassRowMapper.newInstance(Product.class),
                recipeId);
    }

    public Boolean updateRecipe(Recipe recipe) {
        return template.update(
                SQL_UPDATE_RECIPE,
                recipe.recipeName(),
                recipe.recipeCreator(),
                recipe.recipeId()) == 1;
    }

    public Boolean deleteIngredient(String productId) {
        return template.update(
                SQL_DEL_INGREDIENT,
                productId) == 1;
    }
}
