package com.gentooboy.javabom.boardapi.service;

import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.exception.ArticleSaveErrorException;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.request.NewArticle;
import java.util.List;

public interface ArticlesService {

  List<Article> findAllArticles();

  Article findArticleById(Long articleId) throws ArticleNotFoundException;

  Article saveArticle(NewArticle newArticle) throws ArticleSaveErrorException;
}
