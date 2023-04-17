package com.main.backend.recipeshopper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.backend.recipeshopper.model.Product;
import com.main.backend.recipeshopper.service.ShopperService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ShopperController {
    @Autowired
    private ShopperService svc;

    /**
     * Test Endpoint 0: test point for server health check
     * 
     * @return msg from server
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Server is up");
    }

    /**
     * Test Endpoint 1: a test point for making API calls to Django web scrapper
     * 
     * @Return list of Products
     */
    @GetMapping(path = "/parse-url")
    public ResponseEntity<List<Product>> getProductList(
            @RequestParam(required = true) String category) {

        log.info(">>> Test parsing products for category -> %s".formatted(category));

        return ResponseEntity
                .ok(svc.scrapeFromUrl(category.strip().toLowerCase()));
    }

    /**
     * Endpoint 1: a test point for passing html to Django web scrapper for parsing
     * 
     * @return
     */
    @PostMapping(path = "/1")
    public ResponseEntity<Product[]> parseHtml() {
        return null;
    }

    @GetMapping(path = "/2")
    public ResponseEntity<String> getRecipeList() {
        return null;
    }

    @GetMapping(path = "/3")
    public ResponseEntity<String> getRecipeIngredientList() {
        return null;
    }

    @PostMapping(path = "/4")
    public ResponseEntity<Void> postNewRecipe() {
        return null;
    }

    @PutMapping(path = "/5")
    public ResponseEntity<Void> updateRecipe() {
        return null;
    }

    @PostMapping("/6")
    public ResponseEntity<Void> checkoutCart() {
        return null;
    }

}
