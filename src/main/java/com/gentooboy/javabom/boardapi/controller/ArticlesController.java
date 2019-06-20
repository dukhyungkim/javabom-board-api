package com.gentooboy.javabom.boardapi.controller;

import com.gentooboy.javabom.boardapi.constant.ArticleConst;
import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.response.ArticleData;
import com.gentooboy.javabom.boardapi.service.ArticlesService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ArticleConst.CONTEXT_ARTICLE)
@RequiredArgsConstructor
public class ArticlesController {

  private final ArticlesService articlesService;

  @GetMapping("")
  public ResponseEntity<ArticleData> getArticleList() {
    List<Article> allArticles = articlesService.findAllArticles();

    ArticleData articleData = new ArticleData<>(allArticles);

    if (allArticles.isEmpty()) {
      return new ResponseEntity<>(articleData, HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(articleData, HttpStatus.OK);
  }

  @GetMapping("/{articleId}")
  public ResponseEntity<ArticleData> getArticle(@PathVariable final String articleId)
      throws ArticleNotFoundException {
    Article article;

    article = articlesService.findArticleById(articleId);

    return new ResponseEntity<>(new ArticleData<>(article), HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<?> newArticle(@RequestBody final ArticleData<Article> articleData) {
    Article article;

    article = articlesService.saveArticle(articleData.getData());

    return new ResponseEntity<>(new ArticleData<>(article), HttpStatus.CREATED);
  }

  @PutMapping("/{articleId}")
  public ResponseEntity<ArticleData> editArticle(@PathVariable final String articleId,
      @RequestBody final ArticleData<Article> articleData)
      throws ArticleNotFoundException {
    Article article = articlesService.updateArticle(articleId, articleData.getData());

    return new ResponseEntity<>(new ArticleData<>(article), HttpStatus.OK);
  }

  @DeleteMapping("/{articleId}")
  public ResponseEntity<ArticleData> deleteArticle(@PathVariable final String articleId) {
    articlesService.deleteArticle(articleId);

    return new ResponseEntity<>(null, HttpStatus.OK);
  }
}
