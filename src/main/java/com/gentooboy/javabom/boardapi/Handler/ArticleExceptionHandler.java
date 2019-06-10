package com.gentooboy.javabom.boardapi.Handler;

import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.exception.ArticleSaveErrorException;
import com.gentooboy.javabom.boardapi.model.error.Errors;
import com.gentooboy.javabom.boardapi.model.error.Source;
import com.gentooboy.javabom.boardapi.model.response.ErrorData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ArticleExceptionHandler extends ResponseEntityExceptionHandler {

  private final String DATA_TYPE = "/data/type";

  @ExceptionHandler(ArticleNotFoundException.class)
  public ResponseEntity<ErrorData> articleNotFoundHandle(Exception ex, WebRequest request) {
    final String uri = ((ServletWebRequest)request).getRequest().getRequestURI();
    final ArticleNotFoundException exception = (ArticleNotFoundException) ex;
    Errors errors = Errors.builder()
        .status(exception.getStatus())
        .source(new Source(DATA_TYPE + uri))
        .title(exception.getTitle())
        .detail(exception.getDetail())
        .build();
    return new ResponseEntity<>(new ErrorData(errors), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ArticleSaveErrorException.class)
  public ResponseEntity<ErrorData> articleSaveErrorHandle(Exception ex, WebRequest request) {
    final String uri = ((ServletWebRequest)request).getRequest().getRequestURI();
    final ArticleSaveErrorException exception = (ArticleSaveErrorException) ex;
    Errors errors = Errors.builder()
        .status(exception.getStatus())
        .source(new Source(DATA_TYPE + uri))
        .title(exception.getTitle())
        .detail(exception.getDetail())
        .build();
    return new ResponseEntity<>(new ErrorData(errors), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
