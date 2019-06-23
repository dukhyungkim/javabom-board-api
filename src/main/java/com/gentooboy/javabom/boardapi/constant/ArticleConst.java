package com.gentooboy.javabom.boardapi.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleConst {
  public static final String CONTEXT_PATH = "/api/v1";
  public static final String CONTEXT_ARTICLE = "/articles";

  public static final String SOURCE_TYPE = "/data/type";

  public static final String TYPE_ARTICLES = "articles";

  public static final String MESSAGE_NOT_FOUND = "Can't find the article.";
}
