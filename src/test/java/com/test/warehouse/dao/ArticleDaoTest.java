package com.test.warehouse.dao;

import com.test.warehouse.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class ArticleDaoTest extends AbstractDaoTest {

    private ArticleDaoImpl articleDao;

    @BeforeEach
    void init() {
        articleDao = new ArticleDaoImpl(new NamedParameterJdbcTemplate(dataSource));
        populateData();
    }

    private void populateData() {
        List<Article> articles = Collections.singletonList(new Article(1, "screw", 4));
        articleDao.saveAll(articles);
    }

    @Test
    void saveAllTest() {
        // saveAll() is called in @BeforeEach
        List<Article> savedArticles = articleDao.getAll();
        assertNotNull(savedArticles);
        assertEquals(1, savedArticles.size());
    }
}
