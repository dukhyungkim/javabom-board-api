package com.gentooboy.javabom.boardapi.controller;

import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.exception.ArticleSaveErrorException;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.request.NewArticle;
import com.gentooboy.javabom.boardapi.model.request.NewArticleData;
import com.gentooboy.javabom.boardapi.model.response.ArticleData;
import com.gentooboy.javabom.boardapi.model.response.ArticleListData;
import com.gentooboy.javabom.boardapi.service.ArticlesService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles")
public class ArticlesController {

  private final ArticlesService articlesService;

  public ArticlesController(ArticlesService articlesService) {
    this.articlesService = articlesService;
  }

  @GetMapping("")
  public ResponseEntity<ArticleListData> getArticleList() {
    List<Article> allArticles = articlesService.findAllArticles();

    ArticleListData articleListData = new ArticleListData(allArticles);

    if (allArticles.isEmpty()) {
      return new ResponseEntity<>(articleListData, HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(articleListData, HttpStatus.OK);
  }

  @GetMapping("/{articleId}")
  public ResponseEntity<ArticleData> getArticle(@PathVariable Long articleId)
      throws ArticleNotFoundException {
    Article article;

    try {
      article = articlesService.findArticleById(articleId);
    } catch (ArticleNotFoundException e) {
      e.setStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));
      e.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
      throw e;
    }

    return new ResponseEntity<>(new ArticleData(article), HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<ArticleData> getArticle(@RequestBody NewArticleData newArticleData)
      throws ArticleSaveErrorException {
    NewArticle newArticle = newArticleData.getData();
    Article article;

    try {
      article = articlesService.saveArticle(newArticle);
    } catch (ArticleSaveErrorException e) {
      e.setStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      e.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      throw e;
    }

    return new ResponseEntity<>(new ArticleData(article), HttpStatus.CREATED);
  }
}
