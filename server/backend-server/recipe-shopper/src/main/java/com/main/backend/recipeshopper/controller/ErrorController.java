package com.main.backend.recipeshopper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.main.backend.recipeshopper.exceptions.DjangoBadResponseException;
import com.main.backend.recipeshopper.exceptions.IncorrectRequestException;
import com.main.backend.recipeshopper.exceptions.ProductUpsertException;
import com.main.backend.recipeshopper.exceptions.RecipeTransactionException;
import com.main.backend.recipeshopper.utils.Utils;

@RestControllerAdvice(basePackageClasses = ShopperController.class)
public class ErrorController {

        @ExceptionHandler({
                        DjangoBadResponseException.class,
                        ProductUpsertException.class,
                        RecipeTransactionException.class })
        public ResponseEntity<String> handleDjangoErrors(RuntimeException err) {
                return ResponseEntity
                                .internalServerError()
                                .body(Utils.createErrorResponseMsg(err.getMessage()));
        }

        @ExceptionHandler({
                        IncorrectRequestException.class,
        })
        public ResponseEntity<String> handleIllegalRequests(RuntimeException err) {
                return ResponseEntity
                                .badRequest()
                                .body(Utils.createErrorResponseMsg(err.getMessage()));
        }
}
