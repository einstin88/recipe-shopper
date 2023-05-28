package com.main.backend.recipeshopper.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.main.backend.recipeshopper.model.Product;

import static com.main.backend.recipeshopper.database.Queries.*;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate template;

    /**
     * Insert product to DB if it doesn't exist, otherwise product is updated.
     */
    public Boolean upsertProduct(Product product, String category) {
        if (findProductByName(
                product.getName(), product.getPack_size(), category)
                .isEmpty()) {

            return template.update(SQL_INSERT_PRODUCT,
                    product.getProductId(),
                    product.getName(),
                    product.getUrl(),
                    product.getPack_size(),
                    product.getPrice(),
                    product.getImg(),
                    category) == 1;
        } else {
            return template.update(SQL_UPDATE_PRODUCT,
                    product.getUrl(),
                    product.getPrice(),
                    product.getImg(),
                    product.getName(),
                    product.getPack_size(),
                    category) == 1;
        }
    }

    /**
     * Finds a product in the DB that matches the 3 criterion
     */
    public Optional<Product> findProductByName(
            String name, String pack_size, String category) {

        try {
            return Optional.of(
                    template.queryForObject(
                            SQL_FIND_PRODUCT,
                            DataClassRowMapper.newInstance(Product.class),
                            name, pack_size, category));

        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves products from the given category
     */
    public List<Product> findProductsByCategory(
            String category, Integer limit, Integer offset) {

        return template.query(
                SQL_FIND_PRODUCTS,
                DataClassRowMapper.newInstance(Product.class),
                category);
    }

    public List<Product> findProductsByName(String queryString) {
        return template.query(
                SQL_FIND_PRODUCTS_BY_NAME,
                DataClassRowMapper.newInstance(Product.class),
                queryString);
    }
}
