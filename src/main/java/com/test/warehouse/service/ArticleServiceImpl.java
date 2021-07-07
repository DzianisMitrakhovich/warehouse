package com.test.warehouse.service;

import com.test.warehouse.dao.ArticleDao;
import com.test.warehouse.model.Article;
import com.test.warehouse.service.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.test.warehouse.service.util.ErrorLogger.logError;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleDao articleDao;

    @Override
    public void saveArticles(List<Article> articleList) throws ServiceException {
        try {
            articleDao.saveAll(articleList);
            log.info("Successfully saved all articles from inventory");
        } catch (Exception e) {
            String errMsg = "Error saving all articles";
            logError(ArticleServiceImpl.class, e, e.getMessage());
            throw new ServiceException(errMsg);
        }
    }
}
