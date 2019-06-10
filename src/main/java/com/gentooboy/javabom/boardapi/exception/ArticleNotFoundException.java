package com.gentooboy.javabom.boardapi.exception;

public class ArticleNotFoundException extends CustomException {

  public ArticleNotFoundException(String status, String title, String detail) {
    super(status, title, detail);
  }
}
