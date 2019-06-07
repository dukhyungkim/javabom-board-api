package com.gentooboy.javabom.boardapi.exception;

public class ArticleSaveErrorException extends CustomException {

  public ArticleSaveErrorException(String status, String title, String detail) {
    super(status, title, detail);
  }
}
