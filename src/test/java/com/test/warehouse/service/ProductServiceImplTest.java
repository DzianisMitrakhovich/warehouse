package com.test.warehouse.service;

import com.test.warehouse.dao.ProductDao;
import com.test.warehouse.dao.dto.AvailableProductDto;
import com.test.warehouse.dao.exception.InsufficientAmountInStock;
import com.test.warehouse.model.Product;
import com.test.warehouse.model.ProductPart;
import com.test.warehouse.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void saveAllTest() throws ServiceException {
        List<Product> products = getProducts();
        doNothing().when(productDao).saveAll(products);
        productService.saveAll(products);
    }

    @Test
    void saveAllShouldThrowException() {
        List<Product> products = getProducts();
        doThrow(new DataAccessResourceFailureException("Error saving products")).when(productDao).saveAll(products);

        assertThrows(ServiceException.class, () -> productService.saveAll(products));
    }

    @Test
    void listAvailableProductsTest() throws ServiceException {
        when(productDao.listAvailable())
                .thenReturn(List.of(new AvailableProductDto("Bed", 1),
                        new AvailableProductDto("Table", 3)));

        List<AvailableProductDto> availableProductDtoList = productService.listAvailable();

        assertNotNull(availableProductDtoList);
        assertEquals(2, availableProductDtoList.size());
    }

    @Test
    void listAvailableProductsShouldThrowException() {
        when(productDao.listAvailable())
                .thenThrow(new DataRetrievalFailureException("Failed to get available products"));

        assertThrows(ServiceException.class, () -> productService.listAvailable());
    }

    @Test
    void sellProductTest() throws InsufficientAmountInStock, ServiceException {
        doNothing().when(productDao).sellProduct("Table", 2);

        productService.sellProduct("Table", 2);
    }

    @Test
    void sellProductShouldThrowInsufficientAmountException() throws InsufficientAmountInStock {
        String productName = "Chair";
        int amount = 5;
        doThrow(new InsufficientAmountInStock("Not sufficient items in stock"))
                .when(productDao).sellProduct(productName, amount);
        when(productDao.getIfAvailable(productName)).thenReturn(Optional.of(new AvailableProductDto(productName, 3)));

        assertThrows(InsufficientAmountInStock.class, () -> productService.sellProduct(productName, amount));
    }

    @Test
    void sellProductShouldThrowException() throws InsufficientAmountInStock {
        String productName = "Chair";
        int amount = 5;
        doThrow(new DataAccessResourceFailureException("Error selling product"))
                .when(productDao).sellProduct(productName, amount);

        assertThrows(ServiceException.class, () -> productService.sellProduct(productName, amount));
    }

    private List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Bed",
                List.of(new ProductPart(1, 2),
                        new ProductPart(2, 4))));
        products.add(new Product(2, "Table",
                List.of(new ProductPart(3, 8),
                        new ProductPart(4, 1))));
        products.add(new Product(3, "Wardrobe",
                List.of(new ProductPart(3, 8),
                        new ProductPart(4, 1),
                        new ProductPart(2, 2))));
        return products;
    }
}
