package com.gentooboy.javabom.boardapi;

import com.gentooboy.javabom.boardapi.config.ServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoardApiApplication {

  public static void main(String[] args) {
    ServerConfig.setContextPath();
    SpringApplication.run(BoardApiApplication.class, args);
  }

}
