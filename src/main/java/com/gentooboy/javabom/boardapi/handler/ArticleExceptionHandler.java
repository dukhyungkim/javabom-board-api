package com.gentooboy.javabom.boardapi.handler;

import com.gentooboy.javabom.boardapi.constant.ArticleConst;
import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.model.error.Error;
import com.gentooboy.javabom.boardapi.model.error.Source;
import com.gentooboy.javabom.boardapi.model.response.ErrorData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ArticleExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ArticleNotFoundException.class)
  public ResponseEntity<ErrorData> handleArticleNotFound(final ArticleNotFoundException ex,
      final WebRequest request) {
    final String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
    Error error = Error.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .source(new Source(ArticleConst.SOURCE_TYPE + uri))
        .title(HttpStatus.NOT_FOUND.getReasonPhrase())
        .message(ex.getMessage())
        .build();
    return new ResponseEntity<>(new ErrorData(error), HttpStatus.NOT_FOUND);
  }
}
