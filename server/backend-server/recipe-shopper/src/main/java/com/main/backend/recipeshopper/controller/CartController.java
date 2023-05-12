package com.main.backend.recipeshopper.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.backend.recipeshopper.model.Cart;
import com.main.backend.recipeshopper.utils.Urls;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = Urls.URL_PREFIX_API, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CartController {
    
    @PostMapping(path = Urls.URL_CART_CHECKOUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkoutCart(
            @RequestBody Cart cart) {

        log.info("Checking out cart: {}", cart);

        return ResponseEntity.ok().build();
    }
}
