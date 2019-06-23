package com.gentooboy.javabom.boardapi.model.articles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Attributes {

  private String title;
  private String content;
}
