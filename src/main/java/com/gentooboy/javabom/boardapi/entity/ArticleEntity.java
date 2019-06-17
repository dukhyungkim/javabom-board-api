package com.gentooboy.javabom.boardapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "Articles")
@Builder
@Getter
public class ArticleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(min = 1)
  @Column(nullable = false)
  private String title;

  @Column(length = 3000)
  private String content;
}
