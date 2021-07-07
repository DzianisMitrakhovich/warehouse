package com.test.warehouse.dao;

import com.test.warehouse.dao.dto.AvailableProductDto;
import com.test.warehouse.dao.exception.InsufficientAmountInStock;
import com.test.warehouse.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ProductDaoImpl implements ProductDao {

    private static final String NEGATIVE_QUANTITY_ERR_MSG = "Check constraint 'quantity_column_positive' is violated";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<Product> products) {
        String productsSql = "INSERT INTO products (name) VALUES(:name) " +
                "ON DUPLICATE KEY UPDATE name=:name, product_id=LAST_INSERT_ID(product_id)";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        products.forEach(product -> {
            sqlParameterSource.addValue("name", product.getName());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(productsSql, sqlParameterSource, keyHolder, new String[]{"product_id"});
            log.debug("Saved product with id: {}", keyHolder.getKey());
            product.getProductParts().forEach(productPart -> {
                String productPartsSql = "INSERT INTO product_parts (article_id, product_id, amount) VALUES (:article_id, :product_id, :amount) ON DUPLICATE KEY UPDATE amount=:amount";
                namedParameterJdbcTemplate.update(productPartsSql,
                        Map.of("article_id", productPart.getArticleId(),
                                "product_id", Objects.requireNonNull(keyHolder.getKey()),
                                "amount", productPart.getQuantity()));
            });
        });
    }

    @Override
    public List<Product> getAll() {
        String sql = "SELECT * FROM products";
        return namedParameterJdbcTemplate.query(sql, resultSet -> {
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(new Product(resultSet.getInt("product_id"),
                        resultSet.getString("name"), Collections.emptyList()));
            }
            return products;
        });
    }

    @Override
    public List<AvailableProductDto> listAvailable() {
        String sql = "SELECT p.name, MIN(FLOOR(CASE WHEN pp.amount > 0 THEN a.stock / pp.amount ELSE 0 END)) " +
                "AS amount_available FROM products AS p " +
                "JOIN product_parts AS pp ON p.product_id=pp.product_id " +
                "JOIN articles AS a ON a.article_id=pp.article_id " +
                "GROUP BY p.name HAVING amount_available > 0";
        return namedParameterJdbcTemplate.query(sql, resultSet -> {
            List<AvailableProductDto> availableProductDtos = new ArrayList<>();
            while (resultSet.next()) {
                availableProductDtos.add(new AvailableProductDto(resultSet.getString("name"), resultSet.getInt("amount_available")));
            }
            return availableProductDtos;
        });
    }

    @Override
    public Optional<AvailableProductDto> getIfAvailable(String name) {
        String sql = "SELECT p.name, MIN(FLOOR(CASE WHEN pp.amount > 0 THEN a.stock / pp.amount ELSE 0 END)) " +
                "AS amount_available FROM products AS p " +
                "JOIN product_parts AS pp ON p.product_id=pp.product_id " +
                "JOIN articles AS a ON a.article_id=pp.article_id " +
                "GROUP BY p.name HAVING amount_available > 0 AND p.name=:name";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("name", name);
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, resultSet -> {
            if (resultSet.next()) {
                return Optional.of(new AvailableProductDto(resultSet.getString("name"),
                        resultSet.getInt("amount_available")));
            } else return Optional.empty();
        });
    }

    @Override
    public void sellProduct(String name, int amount) throws InsufficientAmountInStock {
        int rowsAffected;
        try {
            MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
            sqlParameterSource.addValue("name", name);
            sqlParameterSource.addValue("amount", amount);
            String sql = "UPDATE articles AS a " +
                    "JOIN product_parts AS pp ON a.article_id=pp.article_id " +
                    "JOIN products AS p ON p.product_id=pp.product_id " +
                    "SET a.stock=(a.stock - :amount * pp.amount) " +
                    "WHERE p.name=:name AND a.article_id=pp.article_id";
            rowsAffected = namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        } catch (DataAccessException e) {
            if (Objects.requireNonNull(e.getCause().getMessage()).startsWith(NEGATIVE_QUANTITY_ERR_MSG)) {
                throw new InsufficientAmountInStock(e);
            } else {
                throw e;
            }
        }
        if (rowsAffected == 0) {
            throw new IncorrectResultSizeDataAccessException(String.format("Failed to sell product. Probably there's no product with name: %s", name), 1);
        }
    }
}
