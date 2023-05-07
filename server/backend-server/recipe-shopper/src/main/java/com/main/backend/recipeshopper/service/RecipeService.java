package com.main.backend.recipeshopper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.main.backend.recipeshopper.exceptions.IncorrectRequestException;
import com.main.backend.recipeshopper.exceptions.RecipeTransactionException;
import com.main.backend.recipeshopper.model.Ingredient;
import com.main.backend.recipeshopper.model.Recipe;
import com.main.backend.recipeshopper.repository.RecipeRepository;
import com.main.backend.recipeshopper.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecipeService {
    @Autowired
    private RecipeRepository repo;

    /**
     * 
     * @param limit
     * @param offset
     * @return
     */
    public List<Recipe<Ingredient>> getRecipeList(Integer limit, Integer offset) {
        log.info(">>> Retrieving recipes from DB...");

        return repo.findRecipes(offset, limit);
    }

    /**
     * 
     * @param recipeId
     * @return
     */
    public Recipe<Ingredient> getRecipeById(String recipeId) {
        return repo.findRecipeById(recipeId)
                .orElseThrow(() -> {
                    return Utils.generateServerError(
                            "Recipe id %s does not exist",
                            IncorrectRequestException.class,
                            recipeId);
                });
    }

    /**
     * 
     * @param recipe
     */
    @Transactional(rollbackFor = { RecipeTransactionException.class })
    public void insertNewRecipe(Recipe<Ingredient> recipe) {

        // Check if recipe by the same user exists already, else throw error
        repo.findRecipeByNameCreator(recipe.recipeName(), recipe.recipeCreator())
                .ifPresent((res) -> {
                    throw Utils.generateServerError(
                            "Recipe '%s' by '%s' already exist",
                            IncorrectRequestException.class,
                            res.recipeName(), res.recipeCreator());
                });

        // Proceed to insert new recipe to both recipe & recipe_ingredients table
        // Handle any errors
        recipe = Utils.generateRecipeId(recipe);

        if (!repo.insertRecipe(recipe))
            throw Utils.generateServerError(
                    "Failed to create recipe",
                    RecipeTransactionException.class);

        if (!repo.insertRecipeIngredients(
                recipe.recipeId(), recipe.ingredients()))
            throw Utils.generateServerError(
                    "Failed to add recipe ingredients",
                    RecipeTransactionException.class);

        // If function runs up to this point, recipe is sucessfully update
    }

    /**
     * 
     * @param recipe
     */
    @Transactional(rollbackFor = { RecipeTransactionException.class })
    public void updateRecipe(Recipe<Ingredient> recipe) {

        String repId = recipe.recipeId();

        // Check if recipe exists, else throw error
        repo.findRecipeById(repId)
                .orElseThrow(() -> {
                    return Utils.generateServerError(
                            "Recipe id: %s by %s does not exist!",
                            IncorrectRequestException.class,
                            repId, recipe.recipeCreator());
                });

        // Proceed to update recipe details and handle any errors
        if (!repo.updateRecipe(recipe)) {
            log.error("--- Recipe not updated");
            throw new RecipeTransactionException("Failed to update recipe");
        }

        // Loop through the old ingredient list
        // if any element has reference in the new list, remove it
        // Else, remove the entry from the db
        List<Ingredient> newIngredients = recipe.ingredients();
        repo.findRecipeIngredients(repId)
                .forEach(ingredient -> {
                    String prodId = ingredient.getProductId();

                    if (!newIngredients.remove(ingredient)) {
                        if (!repo.deleteIngredient(prodId))
                            throw Utils.generateServerError(
                                    "Could not delete ingredient-id=%s from recipe=%s",
                                    RecipeTransactionException.class,
                                    prodId, repId);
                    }
                });

        // Any remaining new ingredients in the list will be added to DB
        if (newIngredients.size() > 0 &&
                !repo.insertRecipeIngredients(repId, newIngredients))
            throw Utils.generateServerError(
                    "Failed to update new ingredients",
                    RecipeTransactionException.class);
                    
        // If function runs up to this point, recipe has been successfully updated
    }
}
