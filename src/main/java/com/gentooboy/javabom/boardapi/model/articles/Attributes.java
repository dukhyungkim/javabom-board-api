package com.gentooboy.javabom.boardapi.model.articles;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Attributes {

  String title;
  String body;

  public Attributes(String title, String body) {
    this.title = title;
    this.body = body;
  }
}
