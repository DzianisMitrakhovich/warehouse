package com.test.warehouse.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @JsonAlias("art_id")
    private int id;
    private String name;
    @JsonAlias("stock")
    private int inStock;
}
