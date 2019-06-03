package com.gentooboy.javabom.boardapi.model.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Errors {

  String status;
  Source source;
  String title;
  String detail;
}
