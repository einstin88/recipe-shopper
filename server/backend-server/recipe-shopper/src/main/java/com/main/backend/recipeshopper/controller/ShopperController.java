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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.main.backend.recipeshopper.model.Cart;
import com.main.backend.recipeshopper.model.Ingredient;
import com.main.backend.recipeshopper.model.Product;
import com.main.backend.recipeshopper.model.Recipe;
import com.main.backend.recipeshopper.service.ProductService;
import com.main.backend.recipeshopper.service.RecipeService;
import com.main.backend.recipeshopper.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ShopperController {
        @Autowired
        private ProductService productSvc;

        @Autowired
        private RecipeService recipeSvc;

        /**
         * Test Endpoint 0: test point for server health check
         * 
         * @return message from server if it's running
         */
        @GetMapping(path = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
        public ResponseEntity<String> healthCheck() {
                return ResponseEntity
                                .ok(Utils.serverOkResponse());
        }

        /**
         * Test Endpoint 1: a test point for making API call to Django web scrapper
         * - errors from api call or product insertion to DB will be handled by
         * ErrorController
         * 
         * @param category (in String) is required and is mapped to a URL for scraping
         * @Return List of Products
         */
        @GetMapping(path = "/parse-url")
        public ResponseEntity<List<Product>> parseUrl(
                        @RequestParam(required = true) String category) {

                log.info(">>> Test parsing products for category -> %s".formatted(category));

                return ResponseEntity
                                .ok(productSvc.scrapeFromUrl(category.strip().toLowerCase()));
        }

        /**
         * Endpoint 1: receive html to pass to Django web scrapper for parsing
         * 
         * @return List of Products
         */
        @PostMapping(path = "/parse-html", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<List<Product>> parseHtml(
                        @RequestParam(required = true) String category,
                        @RequestPart(required = true) MultipartFile file) {

                log.info(">>> Request to parse '%s' in category '%s'".formatted(
                                file.getOriginalFilename(), category));

                return ResponseEntity
                                .ok(productSvc.scrapeFromHtml(category, file.getResource()));
        }

        // Tested
        @GetMapping(path = "/products/{category}")
        public ResponseEntity<List<Product>> getProductList(
                        @PathVariable(required = true) String category,
                        @RequestParam(defaultValue = "20") Integer limit,
                        @RequestParam(defaultValue = "0") Integer offset) {

                log.info(">>> Request for products in category '%s' with limit-%d, offset-%d"
                                .formatted(category, limit, offset));

                return ResponseEntity
                                .ok(productSvc.getProductListByCategory(category, limit, offset));
        }

        @GetMapping(path = "/recipes")
        public ResponseEntity<List<Recipe<Ingredient>>> getRecipeList(
                        @RequestParam(defaultValue = "10") Integer limit,
                        @RequestParam(defaultValue = "0") Integer offset) {

                log.info(">>> Request for recipe list...");

                return ResponseEntity
                                .ok(recipeSvc.getRecipeList(limit, offset));
        }

        @PostMapping(path = "/recipe/new", consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Void> postNewRecipe(
                        @RequestBody Recipe<Ingredient> recipe) {

                log.info(">>> Posting new recipe: " + recipe);
                recipeSvc.insertNewRecipe(recipe);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .build();
        }

        @PutMapping(path = "/recipe/update", consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Void> updateRecipe(
                        @RequestBody Recipe<Ingredient> recipe) {

                log.info(">>> Updating new recipe: " + recipe);
                recipeSvc.updateRecipe(recipe);

                return ResponseEntity
                                .status(HttpStatus.ACCEPTED)
                                .build();
        }

        @PostMapping("/checkout")
        public ResponseEntity<Void> checkoutCart(
                        Cart cart) {

                log.info(">>> Checking out cart: " + cart);
                return null;
        }

}
