package com.test.warehouse.dao;

import com.test.warehouse.dao.dto.AvailableProductDto;
import com.test.warehouse.dao.exception.InsufficientAmountInStock;
import com.test.warehouse.model.Article;
import com.test.warehouse.model.Product;
import com.test.warehouse.model.ProductPart;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
public class ProductDaoTest extends AbstractDaoTest {

    private static final String SMALL_TABLE = "Small table";
    private static final String BIG_TABLE = "Big table";
    private ProductDaoImpl productDao;
    private ArticleDaoImpl articleDao;

    @BeforeEach
    void init() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        productDao = new ProductDaoImpl(namedParameterJdbcTemplate);
        articleDao = new ArticleDaoImpl(namedParameterJdbcTemplate);
        populateData();
    }

    private void populateData() {
        List<Article> articles = Collections.singletonList(new Article(1, "screw", 4));
        articleDao.saveAll(articles);
        List<Product> products = List.of(
                new Product(1, SMALL_TABLE, Collections.singletonList(new ProductPart(1, 3))),
                new Product(1, BIG_TABLE, Collections.singletonList(new ProductPart(1, 6))));
        productDao.saveAll(products);
    }

    @Test
    void saveAllTest() {
        // saveAll() is called in @BeforeEach
        List<Product> savedProducts = productDao.getAll();
        assertNotNull(savedProducts);
        assertEquals(2, savedProducts.size());
    }

    @Test
    void listAvailableTest() {
        List<AvailableProductDto> availableProductDtos = productDao.listAvailable();

        assertNotNull(availableProductDtos);
        assertEquals(1, availableProductDtos.size());
        assertEquals(SMALL_TABLE, availableProductDtos.get(0).getName());
    }

    @Test
    void getIfAvailableTest() {
        Optional<AvailableProductDto> availableProduct = productDao.getIfAvailable(SMALL_TABLE);

        assertTrue(availableProduct.isPresent());
        assertEquals(availableProduct.get().getName(), SMALL_TABLE);

        Optional<AvailableProductDto> notAvailableProduct = productDao.getIfAvailable(BIG_TABLE);
        assertFalse(notAvailableProduct.isPresent());
    }

    @Test
    void sellProductTest() throws InsufficientAmountInStock {
        assertTrue(productDao.getIfAvailable(SMALL_TABLE).isPresent());

        productDao.sellProduct(SMALL_TABLE, 1);

        assertFalse(productDao.getIfAvailable(SMALL_TABLE).isPresent());
    }

    @Test
    void sellProductShouldThrowExceptionTest() {
        assertTrue(productDao.getIfAvailable(SMALL_TABLE).isPresent());
        assertEquals(productDao.getIfAvailable(SMALL_TABLE).get().getAvailableAmount(), 1);

        assertThrows(InsufficientAmountInStock.class, ()-> productDao.sellProduct(SMALL_TABLE, 5));
    }

}
