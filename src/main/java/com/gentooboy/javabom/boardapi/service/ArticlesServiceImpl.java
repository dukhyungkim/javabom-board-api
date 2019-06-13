package com.gentooboy.javabom.boardapi.service;

import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.exception.ArticleSaveErrorException;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.request.NewArticle;
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
  public Article findArticleById(Long articleId) throws ArticleNotFoundException {

    return Article.builder().build();
  }

  @Override
  public Article saveArticle(NewArticle newArticle) throws ArticleSaveErrorException {

    return Article.builder().build();
  }

  @Override
  public Article updateArticle(Long articleId, Article article)
      throws ArticleSaveErrorException, ArticleNotFoundException {

    return Article.builder().build();
  }

  @Override
  public String deleteArticle(String articleId) {
    return articleId;
  }
}
