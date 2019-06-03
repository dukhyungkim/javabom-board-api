package com.gentooboy.javabom.boardapi.controller;

import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.response.ArticleData;
import com.gentooboy.javabom.boardapi.model.response.ArticleListData;
import com.gentooboy.javabom.boardapi.service.ArticlesService;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    ArticleListData articleListData = new ArticleListData();
    articleListData.setData(allArticles);

    if (allArticles.size() == 0) {
      return new ResponseEntity<>(articleListData, HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(articleListData, HttpStatus.OK);
  }

}
