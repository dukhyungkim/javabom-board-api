package com.gentooboy.javabom.boardapi.model.articles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Article {

  private String type;
  private String id;
  private Attributes attributes;
  private Links links;
}
