package com.test.warehouse.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Product {
    private int id;
    private String name;
    @JsonAlias("contain_articles")
    private List<ProductPart> productParts;
}
