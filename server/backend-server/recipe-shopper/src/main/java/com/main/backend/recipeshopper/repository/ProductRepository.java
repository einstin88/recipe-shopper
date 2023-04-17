package com.main.backend.recipeshopper.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.main.backend.recipeshopper.model.Product;

import static com.main.backend.recipeshopper.database.Queries.*;

import java.util.Optional;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate template;

    public Boolean upsertProduct(Product product, String category) {
        int result;
        if (findProductByName(
                product.name(), product.pack_size(), category).isEmpty()) {

            result = template.update(SQL_INSERT_PRODUCT,
                    product.id(),
                    product.name(),
                    product.url(),
                    product.pack_size(),
                    product.price(),
                    product.img(),
                    category);
        } else {
            result = template.update(SQL_UPDATE_PRODUCT,
                    product.url(),
                    product.price(),
                    product.img(),
                    product.name(),
                    product.pack_size(),
                    category);
        }

        if (result != 1)
            return false;

        return true;
    }

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
}
