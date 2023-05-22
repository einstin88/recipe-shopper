package com.main.backend.recipeshopper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.main.backend.recipeshopper.exceptions.DjangoBadResponseException;
import com.main.backend.recipeshopper.exceptions.IncorrectRequestException;
import com.main.backend.recipeshopper.exceptions.ProductUpsertException;
import com.main.backend.recipeshopper.exceptions.RecipeTransactionException;

@RestControllerAdvice(basePackageClasses = { RecipeController.class, ProductController.class })
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
        // @formatter:off
        @ExceptionHandler({
                DjangoBadResponseException.class,
                ProductUpsertException.class,
                RecipeTransactionException.class })
        public ResponseEntity<String> handleInternalErrors(RuntimeException err) {
                return ResponseEntity
                        .internalServerError()
                        .body(err.getMessage());
        }
        // @formatter: on

        /**
         * Generate error responses for exception type(s):
         * {@link IncorrectRequestException}
         * {@link AccessDeniedException}
         * 
         * @param err - the thrown exception instance belonging to a subclass of
         *            RuntimeException
         * @return Response status 400 with error message
         */
        // @formatter:off
        @ExceptionHandler({
                IncorrectRequestException.class
        })
        public ResponseEntity<String> handleIllegalRequests(RuntimeException err) {
                return ResponseEntity
                        .badRequest()
                        .body(err.getMessage());
        }
        // @formatter: on

        // @formatter:off
        @ExceptionHandler({
                AccessDeniedException.class
        })
        public ResponseEntity<String> handleExpiredRequests(RuntimeException err) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(err.getMessage());
        }
        // @formatter: on
}
