package com.main.backend.recipeshopper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.backend.recipeshopper.model.Ingredient;
import com.main.backend.recipeshopper.model.Recipe;
import com.main.backend.recipeshopper.service.RecipeService;
import com.main.backend.recipeshopper.utils.Urls;
import com.main.backend.recipeshopper.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = Urls.URL_PREFIX_API, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RecipeController {
        @Autowired
        private RecipeService recipeSvc;

        /**
         * Test Endpoint 0: test point for server health check
         * 
         * @return message from server if it's running
         */
        @GetMapping(path = Urls.URL_HEALTH, produces = MediaType.TEXT_PLAIN_VALUE)
        public ResponseEntity<String> healthCheck() {
                log.debug(">>> Test point called...");
                return ResponseEntity
                                .ok(Utils.serverOkResponse());
        }

        /**
         * Endpoint 3 (GET): Retrieves the list of recipes
         * 
         * @param limit  (Integer, default = 10) Max number of results
         * @param offset (Integer, default = 0) Starting index of the results to
         *               retrieve
         * @return List of recipes
         */
        @GetMapping(path = Urls.URL_RECIPES)
        public ResponseEntity<List<Recipe<Ingredient>>> getRecipeList(
                        @RequestParam(defaultValue = "10") Integer limit,
                        @RequestParam(defaultValue = "0") Integer offset) {

                log.debug(">>> Request for recipe list...");

                return ResponseEntity
                                .ok(recipeSvc.getRecipeList(limit, offset));
        }

        /**
         * Endpoint 4 (GET): Retrieves a recipe by its ID
         * 
         * @param recipeId (String, mandatory)
         * @return A {@link Recipe} object
         */
        @GetMapping(path = Urls.URL_RECIPE_VIEW + "/{recipeId}")
        public ResponseEntity<Recipe<Ingredient>> getRecipeById(
                        @PathVariable String recipeId) {

                log.debug(">>> Request for recipe with id: {}", recipeId);

                return ResponseEntity
                                .ok(recipeSvc.getRecipeById(recipeId));
        }

        /**
         * Endpoint 5 (POST): Receives a new recipe to be added to the DB
         * - Errors will be handled by {@link ErrorController} and rollback any DB
         * transactions
         * 
         * @see RecipeService
         * 
         * @param recipe - mapped {@link Recipe} object
         * @return Response status 201
         */
        @PostMapping(path = Urls.URL_RECIPE_NEW, consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Void> postNewRecipe(
                        @RequestBody Recipe<Ingredient> recipe) {

                log.debug(">>> Posting new recipe: {}", recipe);
                recipeSvc.insertNewRecipe(recipe);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .build();
        }

        /**
         * Endpoint 6 (PUT): Receives an updated recipe
         * - Errors will be handled by {@link ErrorController} and rollback any DB
         * transactions
         * 
         * @param recipe - mapped {@link Recipe} object
         * @return Response status 202
         */
        @PutMapping(path = Urls.URL_RECIPE_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Void> updateRecipe(
                        @RequestBody Recipe<Ingredient> recipe) {

                log.debug(">>> Updating new recipe: {}", recipe);
                recipeSvc.updateRecipe(recipe);

                return ResponseEntity
                                .status(HttpStatus.ACCEPTED)
                                .build();
        }
}
