package com.main.backend.recipeshopper.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.main.backend.recipeshopper.exceptions.RecipeCreationException;
import com.main.backend.recipeshopper.model.Recipe;
import com.main.backend.recipeshopper.repository.RecipeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecipeService {
    @Autowired
    private RecipeRepository repo;

    public List<Recipe> getRecipeList(Integer limit, Integer offset) {
        try {
            log.info(">>> Retrieving recipes from DB...");
            return repo.findRecipes(offset, limit);

        } catch (DataAccessException e) {
            log.error("--- Error! Could not retrieve recipes from DB");
            return Collections.emptyList();
        }
    }

    @Transactional(rollbackFor = {RecipeCreationException.class})
    public void insertNewRecipe(Recipe recipe) {
        // Check if recipe by the same user exists already, throws error

        // Attempt to insert new recipe to both recipe & recipe_ingredients table
        // Handle any errors
    }
}
