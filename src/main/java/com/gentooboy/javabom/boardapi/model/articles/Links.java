package com.gentooboy.javabom.boardapi.model.articles;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Links {

  String self;

  @Builder
  public Links(String self) {
    this.self = self;
  }
}
