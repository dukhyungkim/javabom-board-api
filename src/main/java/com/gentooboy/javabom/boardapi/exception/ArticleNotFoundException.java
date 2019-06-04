package com.gentooboy.javabom.boardapi.exception;

import lombok.Getter;

@Getter
public class ArticleNotFoundException extends Exception {

  final String status;
  final String title;
  final String detail;

  public ArticleNotFoundException(String status, String title, String detail) {
    this.status = status;
    this.title = title;
    this.detail = detail;
  }
}
