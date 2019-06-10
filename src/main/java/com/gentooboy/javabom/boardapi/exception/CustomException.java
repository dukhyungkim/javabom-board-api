package com.gentooboy.javabom.boardapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
class CustomException extends Exception {

  String status;
  String title;
  String detail;
}
