package com.test.warehouse.web.dto;

import com.test.warehouse.model.Product;
import lombok.Data;

import java.util.List;

@Data
public class ProductWrapper {
    private List<Product> products;
}
