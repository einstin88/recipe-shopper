package com.main.backend.recipeshopper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.main.backend.recipeshopper.model.Product;
import com.main.backend.recipeshopper.service.ProductService;
import com.main.backend.recipeshopper.utils.Urls;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = Urls.URL_PREFIX_API, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProductController {
        @Autowired
        private ProductService productSvc;

        /**
         * Test Endpoint 1: a test point for making API call to Django web scrapper
         * - errors from api call or product insertion to DB will be handled by
         * {@link ErrorController}
         * 
         * @param category (String, mandatory) a parameter mapped to a URL for scraping
         * @return List of Product objects
         */
        @GetMapping(path = Urls.URL_PARSE_URL)
        public ResponseEntity<List<Product>> parseUrl(
                        @RequestParam String category) {

                log.debug(">>> Test parsing products for category -> {}", category);

                return ResponseEntity
                                .ok(productSvc.scrapeFromUrl(category.strip().toLowerCase()));
        }

        /**
         * Endpoint 1 (POST): receive html to pass to Django web scrapper for parsing
         * - errors will be handled by {@link ErrorController} and rollback any DB
         * transactions
         * 
         * @see ProductService
         * 
         * @param category (String, mandatory) parameter to associate with the parsed
         *                 Products when saving to DB
         * @param file     (File, mandatory) HTML file of a supermarket's products
         *                 listing
         * @return List of Product objects
         */
        @PostMapping(path = Urls.URL_PARSE_HTML, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<List<Product>> parseHtml(
                        @RequestParam String category,
                        @RequestPart MultipartFile file) {

                log.debug(">>> Request to parse '{}' in category '{}'",
                                file.getOriginalFilename(), category);

                return ResponseEntity
                                .ok(productSvc.scrapeFromHtml(category, file.getResource()));
        }

        /**
         * Endpoint 2 (GET): Retrieves a list of products from the given category
         * 
         * @param category (String, mandatory) The category of products to retrieve from
         *                 DB
         * @param limit    (Integer, default = 20) Max number of results
         * @param offset   (Integer, default = 0) Starting index of the results to
         *                 retrieve
         * @return List of products
         */
        @GetMapping(path = Urls.URL_PRODUCTS + "/{category}")
        public ResponseEntity<List<Product>> getProductList(
                        @PathVariable String category,
                        @RequestParam(defaultValue = "20") Integer limit,
                        @RequestParam(defaultValue = "0") Integer offset) {

                log.debug(">>> Request for products in category '{}' with limit-{}, offset-{}",
                                category, limit, offset);

                return ResponseEntity
                                .ok(productSvc.getProductListByCategory(category, limit, offset));
        }

}
