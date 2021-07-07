package com.test.warehouse.web.dto;

import com.test.warehouse.model.Article;
import lombok.Data;

import java.util.List;

@Data
public class Inventory {
    private List<Article> inventory;
}
