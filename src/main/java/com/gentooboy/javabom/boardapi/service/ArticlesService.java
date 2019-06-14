package com.gentooboy.javabom.boardapi.service;

import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import java.util.List;

public interface ArticlesService {

  List<Article> findAllArticles();

  Article findArticleById(String articleId) throws ArticleNotFoundException;

  Article saveArticle(Article newArticle);

  Article updateArticle(String articleId, Article article) throws ArticleNotFoundException;

  String deleteArticle(String articleId);
}
