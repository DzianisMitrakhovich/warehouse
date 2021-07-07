package com.test.warehouse.dao;

import com.test.warehouse.dao.dto.AvailableProductDto;
import com.test.warehouse.dao.exception.InsufficientAmountInStock;
import com.test.warehouse.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    void saveAll(List<Product> products);

    List<Product> getAll();

    List<AvailableProductDto> listAvailable();

    Optional<AvailableProductDto> getIfAvailable(String name);

    void sellProduct(String name, int amount) throws InsufficientAmountInStock;
}
