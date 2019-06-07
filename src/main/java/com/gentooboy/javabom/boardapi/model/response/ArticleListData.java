package com.gentooboy.javabom.boardapi.model.response;

import com.gentooboy.javabom.boardapi.model.articles.Article;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleListData {

  private List<Article> data;
}
