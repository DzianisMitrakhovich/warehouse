package com.test.warehouse.dao;

import com.test.warehouse.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ArticleDaoImpl implements ArticleDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<Article> articles) {
        String sql = "INSERT INTO articles (article_id, name, stock) VALUES( :id, :name, :stock) " +
                "ON DUPLICATE KEY UPDATE name=:name, stock=:stock";
        Map<String, String> paramMap = new HashMap<>();
        for (Article article : articles) {
            paramMap.put("id", String.valueOf(article.getId()));
            paramMap.put("name", article.getName());
            paramMap.put("stock", String.valueOf(article.getInStock()));
            namedParameterJdbcTemplate.update(sql, paramMap);
        }
    }

    @Override
    public List<Article> getAll() {
        String sql = "SELECT * FROM articles";
        return namedParameterJdbcTemplate.query(sql, resultSet -> {
            List<Article> articles = new ArrayList<>();
            while (resultSet.next()) {
                articles.add(new Article(resultSet.getInt("article_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("stock")));
            }
            return articles;
        });
    }
}
