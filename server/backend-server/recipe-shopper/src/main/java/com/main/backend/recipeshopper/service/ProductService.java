package com.main.backend.recipeshopper.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;
import com.main.backend.recipeshopper.exceptions.DjangoBadResponseException;
import com.main.backend.recipeshopper.exceptions.IncorrectRequestException;
import com.main.backend.recipeshopper.exceptions.ProductUpsertException;
import com.main.backend.recipeshopper.model.Product;
import com.main.backend.recipeshopper.repository.ProductRepository;
import com.main.backend.recipeshopper.utils.Urls;
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

    /**
     * A function to be called by the controller OR at scheduled interval to scrape
     * for products from a given category via an external Django-served backend.
     * Results are saved to the DB.
     * 
     * @throws IncorrectRequestException
     * @throws DjangoBadResponseException
     * @throws ProductUpsertException
     */
    @Transactional(rollbackFor = { ProductUpsertException.class })
    public List<Product> scrapeFromUrl(String category) {
        // Checks if category is valid
        validateCategory(category);

        // Build URL for API call to Django
        String url = UriComponentsBuilder
                .fromHttpUrl(DJANGO_URL)
                .path(Urls.URL_PARSE_URL)
                .queryParam("category", category)
                .build().toString();
        log.debug(">>> API URL to call: {}", url);

        // Build Oauth2 Request to Scrapper server
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

            IdTokenCredentials token = IdTokenCredentials.newBuilder()
                    .setIdTokenProvider((IdTokenProvider) credentials)
                    .setTargetAudience(url)
                    .build();

            GenericUrl genericUrl = new GenericUrl(url);
            HttpCredentialsAdapter adapter = new HttpCredentialsAdapter(token);
            HttpTransport transport = new NetHttpTransport();
            HttpRequest request = transport
                    .createRequestFactory(adapter)
                    .buildGetRequest(genericUrl)
                    .setReadTimeout(0);

            // Call API
            return handleDjangoCallback(category, request);

        } catch (IOException e) {
            throw new IncorrectRequestException(e.getMessage());
        }
    }

    /**
     * A function called by the controller to scrape for products from the given
     * html file via an external Django backend. Results are saved on DB.
     * 
     * @throws IncorrectRequestException
     * @throws DjangoBadResponseException
     * @throws ProductUpsertException
     */
    @Transactional(rollbackFor = { ProductUpsertException.class })
    public List<Product> scrapeFromHtml(String category, Resource file) {
        // Validate given category
        validateCategory(category);

        // Build URL for API call to Django
        String url = UriComponentsBuilder
                .fromHttpUrl(DJANGO_URL)
                .pathSegment(Urls.URL_PARSE_HTML)
                .build().toString();

        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

            IdTokenCredentials token = IdTokenCredentials.newBuilder()
                    .setIdTokenProvider((IdTokenProvider) credentials)
                    .setTargetAudience(url)
                    .build();

            // Build body as form-data
            MultiValueMap<String, Object> part = new LinkedMultiValueMap<>();
            part.add("category", category);
            // body.add("file", file);

            HttpContent body = new MultipartContent()
                    .setContentParts(List.of(
                            new UrlEncodedContent(part),
                            new FileContent(MediaType.TEXT_HTML_VALUE, file.getFile())));

            GenericUrl genericUrl = new GenericUrl(url);
            HttpCredentialsAdapter adapter = new HttpCredentialsAdapter(token);
            HttpTransport transport = new NetHttpTransport();
            HttpRequest request = transport
                    .createRequestFactory(adapter)
                    .buildPostRequest(genericUrl, body)
                    .setReadTimeout(0);

            // Call API
            return handleDjangoCallback(category, request);

        } catch (IOException e) {
            throw new IncorrectRequestException(e.getMessage());
        }

        // ---- Rest Template method -----
        // Build request entity (content type is set implicitly)
        // RequestEntity<MultiValueMap<String, Object>> request = RequestEntity
        // .post(url)
        // .accept(MediaType.APPLICATION_JSON)
        // .body(body);
    }

    /**
     * Retrieve the products within a given category
     */
    public List<Product> getProductListByCategory(
            String category, Integer limit, Integer offset) {

        validateCategory(category);

        return repo.findProductsByCategory(category, limit, offset);
    }

    public List<Product> getProductListByName(String name) {
        return repo.findProductsByName(
                Utils.formatSqlQuery(name));
    }

    /**
     * Internal use.
     * An abstracted function to check if the supplied category is one of the
     * accepted categories.
     */
    private void validateCategory(String category) {
        if (!PRODUCT_CATEGORIES.contains(category)) {
            throw Utils.generateServerError(
                    "'%s' is not valid category",
                    IncorrectRequestException.class,
                    category);
        }
    }

    /**
     * Internal use.
     * An abstracted function to handle API call to Django scrapper server and
     * marshalling the Json response to a List of Product to be inserted into the
     * DB.
     */
    private List<Product> handleDjangoCallback(
            String category, HttpRequest request) {

        // Make API call and handle any errors
        try {
            HttpResponse response = request.execute();

            // Check response status code and content exists
            if (response.getStatusCode() != 200)
                throw Utils.generateServerError(
                        "Django backend responded with status: %d",
                        DjangoBadResponseException.class,
                        response.getStatusCode());

            if (response.getContent() == null)
                throw Utils.generateServerError(
                        "Missing response body fron Django server...",
                        DjangoBadResponseException.class);

            // Parse response into products
            List<Product> products = Utils.parseForProducts(response.parseAsString())
                    .peek(product -> {
                        if (!repo.upsertProduct(product, category))
                            throw Utils.generateServerError(
                                    "Failed to upsert category-%s product: %s",
                                    ProductUpsertException.class,
                                    category, product);
                    })
                    .toList();

            log.debug(">>> Products saved to DB: {}", products.size());

            return products;

        } catch (IOException e) {
            // Handle unexpected errors from API call
            throw Utils.generateServerError(
                    "Request URL is invalid or Django backend is down: %s",
                    DjangoBadResponseException.class,
                    e.getMessage());
        }

    }
}
