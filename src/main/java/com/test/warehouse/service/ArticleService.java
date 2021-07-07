package com.test.warehouse.service;

import com.test.warehouse.model.Article;
import com.test.warehouse.service.exception.ServiceException;

import java.util.List;

public interface ArticleService {

    void saveArticles(List<Article> articleList) throws ServiceException;
}
