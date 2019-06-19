package com.gentooboy.javabom.boardapi.service;

import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import java.util.List;

public interface ArticlesService {

  List<Article> findAllArticles();

  Article findArticleById(final String articleId) throws ArticleNotFoundException;

  Article saveArticle(final Article newArticle);

  Article updateArticle(final String articleId, Article article) throws ArticleNotFoundException;

  void deleteArticle(final String articleId);
}
