package com.main.backend.recipeshopper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.main.backend.recipeshopper.exceptions.DjangoBadResponseException;
import com.main.backend.recipeshopper.exceptions.IncorrectRequestException;
import com.main.backend.recipeshopper.exceptions.ProductUpsertException;
import com.main.backend.recipeshopper.exceptions.RecipeTransactionException;

@RestControllerAdvice(basePackageClasses = ShopperController.class)
public class ErrorController {

        /**
         * Generate error responses for exception types:
         * {@link DjangoBadResponseException},
         * {@link ProductUpsertException},
         * {@link RecipeTransactionException}
         * 
         * @param err - the thrown exception instance belonging to a subclass of
         *            RuntimeException
         * @return Response status 500 with error message
         */
        @ExceptionHandler({
                        DjangoBadResponseException.class,
                        ProductUpsertException.class,
                        RecipeTransactionException.class })
        public ResponseEntity<String> handleInternalErrors(RuntimeException err) {
                return ResponseEntity
                                .internalServerError()
                                .body(err.getMessage());
        }

        /**
         * Generate error responses for exception type(s):
         * {@link IncorrectRequestException}
         * 
         * @param err - the thrown exception instance belonging to a subclass of
         *            RuntimeException
         * @return Response status 400 with error message
         */
        @ExceptionHandler({
                        IncorrectRequestException.class,
        })
        public ResponseEntity<String> handleIllegalRequests(RuntimeException err) {
                return ResponseEntity
                                .badRequest()
                                .body(err.getMessage());
        }
}
