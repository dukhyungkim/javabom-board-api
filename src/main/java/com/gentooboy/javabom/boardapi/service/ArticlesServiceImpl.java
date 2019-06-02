package com.gentooboy.javabom.boardapi.service;

import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.articles.Attributes;
import com.gentooboy.javabom.boardapi.model.articles.Links;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ArticlesServiceImpl implements ArticlesService {

  public List<Article> findAllArticles() {
    List<Article> articleList = new ArrayList<>();

    return articleList;
  }

}
