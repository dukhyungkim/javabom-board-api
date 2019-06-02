package com.gentooboy.javabom.boardapi.service;

import com.gentooboy.javabom.boardapi.model.articles.Article;
import java.util.List;

public interface ArticlesService {

  List<Article> findAllArticles();

}
