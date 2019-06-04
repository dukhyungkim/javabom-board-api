package com.gentooboy.javabom.boardapi.model.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Source {

  String pointer;

  public Source(String pointer) {
    this.pointer = pointer;
  }
}
