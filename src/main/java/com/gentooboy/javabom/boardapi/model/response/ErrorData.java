package com.gentooboy.javabom.boardapi.model.response;

import com.gentooboy.javabom.boardapi.model.error.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorData {

  private Error error;
}
