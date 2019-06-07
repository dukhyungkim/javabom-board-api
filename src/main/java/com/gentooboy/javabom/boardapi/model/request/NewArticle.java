package com.gentooboy.javabom.boardapi.model.request;

import com.gentooboy.javabom.boardapi.model.articles.Attributes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NewArticle {
  String type;
  Attributes attributes;
}
