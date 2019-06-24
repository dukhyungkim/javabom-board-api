package com.gentooboy.javabom.boardapi.model.articles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Article {

  private String type;
  private String id;
  private Attributes attributes;
  private Links links;
}
