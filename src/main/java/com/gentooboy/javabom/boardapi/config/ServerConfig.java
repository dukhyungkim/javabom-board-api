package com.gentooboy.javabom.boardapi.config;

import com.gentooboy.javabom.boardapi.constant.ArticleConst;

public class ServerConfig {

  public static void setContextPath() {
    System.setProperty("server.servlet.context-path", ArticleConst.CONTEXT_PATH);
  }
}
