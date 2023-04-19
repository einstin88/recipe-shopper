package com.main.backend.recipeshopper.service;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.main.backend.recipeshopper.exceptions.DjangoBadResponseException;
import com.main.backend.recipeshopper.exceptions.IllegalRequestException;
import com.main.backend.recipeshopper.exceptions.ProductUpsertException;
import com.main.backend.recipeshopper.model.Product;
import com.main.backend.recipeshopper.repository.ProductRepository;
import com.main.backend.recipeshopper.utils.Utils;

import static com.main.backend.recipeshopper.utils.Constants.*;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {
    @Value("${SCRAPPER_URL}")
    private String DJANGO_URL;

    @Autowired
    private ProductRepository repo;

    @Transactional(rollbackFor = { ProductUpsertException.class })
    public List<Product> scrapeFromUrl(String category) {
        // Checks if category is valid
        validateCategory(category);

        // Build URL for API call to Django
        URI url = UriComponentsBuilder
                .fromHttpUrl(DJANGO_URL)
                .pathSegment(DJ_PARSE_URL)
                .queryParam("category", category)
                .build().toUri();
        log.info(">>> API URL to call: %s".formatted(url.toString()));

        // Build request entity
        RequestEntity<Void> request = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        // Call API
        return handleDjangoCallback(category, request);
    }

    @Transactional(rollbackFor = { ProductUpsertException.class })
    public List<Product> scrapeFromHtml(String category, Resource file) {
        // Validate given category
        validateCategory(category);

        // Build URL for API call to Django
        URI url = UriComponentsBuilder
                .fromHttpUrl(DJANGO_URL)
                .pathSegment(DJ_PARSE_HTML)
                .build().toUri();

        // Build body as form-data
        MultipartBodyBuilder body = new MultipartBodyBuilder();
        body.part("category", category);
        body.part("file", file);

        // Build request entity (content type is set implicitly)
        RequestEntity<MultiValueMap<String, HttpEntity<?>>> request = RequestEntity
                .post(url)
                .accept(MediaType.APPLICATION_JSON)
                .body(body.build());

        return handleDjangoCallback(category, request);
    }

    private void validateCategory(String category) {
        if (!PRODUCT_CATEGORIES.contains(category)) {
            String errMsg = "'%s' is not valid category".formatted(category);
            log.error("--- " + errMsg);
            throw new IllegalRequestException(errMsg);
        }
    }

    private List<Product> handleDjangoCallback(
            String category, RequestEntity<?> request) {
        // Make API call and handle any errors
        ResponseEntity<String> response;
        RestTemplate client = new RestTemplate();
        try {
            response = client.exchange(request, String.class);

            // Check response status code and content
            if (response.getStatusCode() != HttpStatus.OK)
                throw new DjangoBadResponseException(
                        "Django backend reponsed with status: " + response.getStatusCode().toString());
            if (!response.hasBody())
                throw new DjangoBadResponseException(
                        "Missing response body fron Django backend...");

            // Else, log the response and let the logic flow through
            log.debug(">>> Response: \n" + response.getBody());

        } catch (RestClientException e) {
            // Handle unexpected errors from API call
            log.error("--- Error response from Django API -> " + e.getMessage());
            throw new DjangoBadResponseException(
                    "Request URL is invalid or Django backend is down...");
        }

        // Parse response into products
        List<Product> products = Utils.parseForProducts(response.getBody())
                .peek(product -> {
                    if (!repo.upsertProduct(product, category)) {
                        throw new ProductUpsertException(
                                "Failed to upsert category-%s product: %s".formatted(
                                        category, product));
                    }
                })
                .toList();

        log.debug(String.valueOf(products.size()));

        return products;
    }
}
