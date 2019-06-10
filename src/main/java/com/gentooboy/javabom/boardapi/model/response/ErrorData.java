package com.gentooboy.javabom.boardapi.model.response;

import com.gentooboy.javabom.boardapi.model.error.Errors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorData {

  Errors errors;
}
