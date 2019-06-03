package com.gentooboy.javabom.boardapi.model.response;

import com.gentooboy.javabom.boardapi.model.error.Errors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorData {

  Errors errors;

  public ErrorData(Errors errors) {
    this.errors = errors;
  }
}
