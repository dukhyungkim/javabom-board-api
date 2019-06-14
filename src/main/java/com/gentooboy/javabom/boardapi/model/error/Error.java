package com.gentooboy.javabom.boardapi.model.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Error {

  private int status;
  private Source source;
  private String title;
  private String message;
}
