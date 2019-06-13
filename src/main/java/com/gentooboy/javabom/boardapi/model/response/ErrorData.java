package com.gentooboy.javabom.boardapi.model.response;

import com.gentooboy.javabom.boardapi.model.error.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorData {

  private Error error;
}
