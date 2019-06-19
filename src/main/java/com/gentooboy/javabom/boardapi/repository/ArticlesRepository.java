package com.gentooboy.javabom.boardapi.repository;

import com.gentooboy.javabom.boardapi.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlesRepository extends JpaRepository<ArticleEntity, Long> {

}
