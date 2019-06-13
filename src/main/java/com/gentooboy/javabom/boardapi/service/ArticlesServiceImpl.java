package com.gentooboy.javabom.boardapi.service;

import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ArticlesServiceImpl implements ArticlesService {

  @Override
  public List<Article> findAllArticles() {

    return new ArrayList<>();
  }

  @Override
  public Article findArticleById(String articleId) throws ArticleNotFoundException {

    return Article.builder().build();
  }

  @Override
  public Article saveArticle(Article newArticle) {

    return Article.builder().build();
  }

  @Override
  public Article updateArticle(String articleId, Article article) throws ArticleNotFoundException {

    return Article.builder().build();
  }

  @Override
  public String deleteArticle(String articleId) {
    return articleId;
  }
}
