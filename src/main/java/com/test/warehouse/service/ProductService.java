package com.test.warehouse.service;

import com.test.warehouse.dao.dto.AvailableProductDto;
import com.test.warehouse.dao.exception.InsufficientAmountInStock;
import com.test.warehouse.model.Product;
import com.test.warehouse.service.exception.ServiceException;

import java.util.List;

public interface ProductService {
    void saveAll(List<Product> products) throws ServiceException;

    List<AvailableProductDto> listAvailable() throws ServiceException;

    void sellProduct(String name, int amount) throws ServiceException, InsufficientAmountInStock;
}
