package com.gentooboy.javabom.boardapi.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.gentooboy.javabom.boardapi.entity.ArticleEntity;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ArticleRepositoryTest {

  @Autowired
  private ArticleRepository articleRepository;

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

    articleRepository.deleteAll();
  }

  @Test
  public void whenFindAll_thenReturnArticles() {
    articleRepository.save(articleEntity1);
    articleRepository.save(articleEntity2);

    List<ArticleEntity> foundList = articleRepository.findAll();
    assertThat(foundList.size()).isEqualTo(2);

    ArticleEntity found = foundList.get(0);
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(articleEntity1.getId());
    assertThat(found.getTitle()).isEqualTo(articleEntity1.getTitle());
    assertThat(found.getContent()).isEqualTo(articleEntity1.getContent());

    found = foundList.get(1);
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(articleEntity2.getId());
    assertThat(found.getTitle()).isEqualTo(articleEntity2.getTitle());
    assertThat(found.getContent()).isEqualTo(articleEntity2.getContent());

  }

  @Test
  public void whenFindById_thenReturnArticle() {
    articleRepository.save(articleEntity1);
    articleRepository.save(articleEntity2);

    Optional<ArticleEntity> entityOptional = articleRepository.findById(articleEntity2.getId());
    assertThat(entityOptional.isPresent()).isTrue();

    ArticleEntity found = entityOptional.orElse(null);
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(articleEntity2.getId());
    assertThat(found.getTitle()).isEqualTo(articleEntity2.getTitle());
    assertThat(found.getContent()).isEqualTo(articleEntity2.getContent());
  }

  @Test
  public void whenSave_thenUpdateArticle() {
    articleRepository.save(articleEntity1);
    articleRepository.save(articleEntity2);

    ArticleEntity newArticleEntity = ArticleEntity.builder()
        .id(articleEntity1.getId())
        .title("new article")
        .content("new content")
        .build();

    articleRepository.save(newArticleEntity);

    Optional<ArticleEntity> entityOptional = articleRepository.findById(newArticleEntity.getId());
    assertThat(entityOptional.isPresent()).isTrue();

    ArticleEntity found = entityOptional.orElse(null);
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(newArticleEntity.getId());
    assertThat(found.getTitle()).isEqualTo(newArticleEntity.getTitle());
    assertThat(found.getContent()).isEqualTo(articleEntity1.getContent());
  }

  @Test
  public void whenSaveOnlyTitle_thenUpdateOnlyTitle() {
    articleRepository.save(articleEntity1);
    articleRepository.save(articleEntity2);

    ArticleEntity newArticleEntity = ArticleEntity.builder()
        .id(articleEntity1.getId())
        .title("new article")
        .build();

    articleRepository.save(newArticleEntity);

    Optional<ArticleEntity> entityOptional = articleRepository.findById(articleEntity1.getId());
    assertThat(entityOptional.isPresent()).isTrue();

    ArticleEntity found = entityOptional.orElse(null);
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(newArticleEntity.getId());
    assertThat(found.getTitle()).isEqualTo(newArticleEntity.getTitle());
    assertThat(found.getContent()).isEqualTo(articleEntity1.getContent());
  }

  @Test(expected = ConstraintViolationException.class)
  public void whenSaveEmptyTitle_thenThrowException() {
    ArticleEntity newArticleEntity = ArticleEntity.builder()
        .id(articleEntity1.getId())
        .title("")
        .build();

    articleRepository.save(newArticleEntity);
  }

  @Test
  public void whenDelete_thenDeleteArticle() {
    articleRepository.save(articleEntity1);
    articleRepository.save(articleEntity2);

    articleRepository.delete(articleEntity1);

    List<ArticleEntity> foundList = articleRepository.findAll();
    assertThat(foundList.size()).isEqualTo(1);

    ArticleEntity found = foundList.get(0);
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(articleEntity2.getId());
    assertThat(found.getTitle()).isEqualTo(articleEntity2.getTitle());
    assertThat(found.getContent()).isEqualTo(articleEntity2.getContent());

    Optional<ArticleEntity> entityOptional = articleRepository.findById(articleEntity1.getId());
    assertThat(entityOptional.isPresent()).isFalse();
  }

  @Test
  public void whenDeleteAll_thenDeleteAllArticles() {
    articleRepository.save(articleEntity1);
    articleRepository.save(articleEntity2);

    articleRepository.deleteAll();

    List<ArticleEntity> foundList = articleRepository.findAll();
    assertThat(foundList.size()).isEqualTo(0);
  }
}
