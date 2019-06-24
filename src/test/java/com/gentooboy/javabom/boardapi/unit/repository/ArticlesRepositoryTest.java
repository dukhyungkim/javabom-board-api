package com.gentooboy.javabom.boardapi.unit.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.gentooboy.javabom.boardapi.entity.ArticleEntity;
import com.gentooboy.javabom.boardapi.repository.ArticlesRepository;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ArticlesRepositoryTest {

  @Autowired
  private ArticlesRepository articlesRepository;

  private ArticleEntity articleEntity1;
  private ArticleEntity articleEntity2;

  @Before
  public void setUp() {
    articleEntity1 = ArticleEntity.builder()
        .title("Initial Article")
        .content("This is content of article")
        .build();
    articleEntity2 = ArticleEntity.builder()
        .title("Second Article")
        .content("This is other article")
        .build();

    articlesRepository.deleteAll();
  }

  @Test
  public void whenFindAll_thenReturnArticles() {
    articlesRepository.save(articleEntity1);
    articlesRepository.save(articleEntity2);

    List<ArticleEntity> foundList = articlesRepository.findAll();
    assertThat(foundList.size()).isEqualTo(2);

    ArticleEntity found1 = foundList.get(0);
    assertThat(found1).isNotNull();
    assertThat(found1.getId()).isEqualTo(articleEntity1.getId());
    assertThat(found1.getTitle()).isEqualTo(articleEntity1.getTitle());
    assertThat(found1.getContent()).isEqualTo(articleEntity1.getContent());

    ArticleEntity found2 = foundList.get(1);
    assertThat(found2).isNotNull();
    assertThat(found2.getId()).isEqualTo(articleEntity2.getId());
    assertThat(found2.getTitle()).isEqualTo(articleEntity2.getTitle());
    assertThat(found2.getContent()).isEqualTo(articleEntity2.getContent());
  }

  @Test
  public void whenFindById_thenReturnArticle() {
    articlesRepository.save(articleEntity1);
    articlesRepository.save(articleEntity2);

    Optional<ArticleEntity> entityOptional = articlesRepository.findById(articleEntity2.getId());
    assertThat(entityOptional.isPresent()).isTrue();

    ArticleEntity found = entityOptional.orElse(null);
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(articleEntity2.getId());
    assertThat(found.getTitle()).isEqualTo(articleEntity2.getTitle());
    assertThat(found.getContent()).isEqualTo(articleEntity2.getContent());
  }

  @Test
  public void whenSave_thenUpdateArticle() {
    articlesRepository.save(articleEntity1);
    articlesRepository.save(articleEntity2);

    ArticleEntity newArticleEntity = ArticleEntity.builder()
        .id(articleEntity1.getId())
        .title("new article")
        .content("new content")
        .build();

    articlesRepository.save(newArticleEntity);

    Optional<ArticleEntity> entityOptional = articlesRepository.findById(newArticleEntity.getId());
    assertThat(entityOptional.isPresent()).isTrue();

    ArticleEntity found = entityOptional.orElse(null);
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(newArticleEntity.getId());
    assertThat(found.getTitle()).isEqualTo(newArticleEntity.getTitle());
    assertThat(found.getContent()).isEqualTo(articleEntity1.getContent());
  }

  @Test
  public void whenSaveOnlyTitle_thenUpdateOnlyTitle() {
    articlesRepository.save(articleEntity1);
    articlesRepository.save(articleEntity2);

    ArticleEntity newArticleEntity = ArticleEntity.builder()
        .id(articleEntity1.getId())
        .title("new article")
        .build();

    articlesRepository.save(newArticleEntity);

    Optional<ArticleEntity> entityOptional = articlesRepository.findById(articleEntity1.getId());
    assertThat(entityOptional.isPresent()).isTrue();

    ArticleEntity found = entityOptional.orElse(null);
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(newArticleEntity.getId());
    assertThat(found.getTitle()).isEqualTo(newArticleEntity.getTitle());
    assertThat(found.getContent()).isEqualTo(articleEntity1.getContent());
  }

  @Test
  public void whenDelete_thenDeleteArticle() {
    articlesRepository.save(articleEntity1);
    articlesRepository.save(articleEntity2);

    articlesRepository.delete(articleEntity1);

    List<ArticleEntity> foundList = articlesRepository.findAll();
    assertThat(foundList.size()).isEqualTo(1);

    ArticleEntity found = foundList.get(0);
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(articleEntity2.getId());
    assertThat(found.getTitle()).isEqualTo(articleEntity2.getTitle());
    assertThat(found.getContent()).isEqualTo(articleEntity2.getContent());

    Optional<ArticleEntity> entityOptional = articlesRepository.findById(articleEntity1.getId());
    assertThat(entityOptional.isPresent()).isFalse();
  }

  @Test
  public void whenDeleteAll_thenDeleteAllArticles() {
    articlesRepository.save(articleEntity1);
    articlesRepository.save(articleEntity2);

    articlesRepository.deleteAll();

    List<ArticleEntity> foundList = articlesRepository.findAll();
    assertThat(foundList.size()).isEqualTo(0);
  }
}
