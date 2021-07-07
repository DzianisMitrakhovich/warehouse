package com.test.warehouse.dao;

import com.test.warehouse.model.Article;

import java.util.List;

public interface ArticleDao {
    void saveAll(List<Article> articles);
    List<Article> getAll();
}
