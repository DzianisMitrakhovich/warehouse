package com.test.warehouse.service;

import com.test.warehouse.dao.ArticleDao;
import com.test.warehouse.model.Article;
import com.test.warehouse.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {

    @Mock
    private ArticleDao articleDao;
    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    void saveAllTest() throws ServiceException {
        List<Article> articles = getArticles();
        doNothing().when(articleDao).saveAll(articles);

        articleService.saveArticles(articles);
    }

    @Test
    void saveAllShouldThrowException() {
        List<Article> articles = getArticles();
        doThrow(new DataAccessResourceFailureException("Error saving all articles")).when(articleDao).saveAll(articles);

        assertThrows(ServiceException.class, () -> articleService.saveArticles(articles));
    }

    private List<Article> getArticles() {
        return List.of(new Article(1, "screw", 20),
                new Article(2, "leg", 16),
                new Article(3, "seat", 8));
    }
}
