package com.gentooboy.javabom.boardapi.model.response;

import com.gentooboy.javabom.boardapi.model.articles.Article;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleListData {

  private List<Article> data = new ArrayList<>();
}
