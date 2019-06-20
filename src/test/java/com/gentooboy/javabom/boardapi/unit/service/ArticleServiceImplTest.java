package com.gentooboy.javabom.boardapi.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gentooboy.javabom.boardapi.constant.ArticleConst;
import com.gentooboy.javabom.boardapi.entity.ArticleEntity;
import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.articles.Attributes;
import com.gentooboy.javabom.boardapi.repository.ArticlesRepository;
import com.gentooboy.javabom.boardapi.service.ArticlesService;
import com.gentooboy.javabom.boardapi.service.ArticlesServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ArticleServiceImplTest {

  @MockBean
  private ArticlesRepository articlesRepository;

  private ArticlesService articlesService;

  private ArticleEntity savedArticleEntity1;
  private ArticleEntity savedArticleEntity2;
  private ArticleEntity willUpdateArticleEntity;
  private ArticleEntity willSaveArticleEntity;

  private String savedArticleEntityLink1;
  private String savedArticleEntityLink2;
  private String willUpdateArticleEntityLink;
  private String willSaveArticleEntityLink;

  @Before
  public void setUp() {
    savedArticleEntity1 = ArticleEntity.builder()
        .id(1L)
        .title("article title")
        .content("article content")
        .build();
    savedArticleEntity2 = ArticleEntity.builder()
        .id(2L)
        .title("article title")
        .content("article content")
        .build();
    willUpdateArticleEntity = ArticleEntity.builder()
        .id(2L)
        .title("update title")
        .content("update content")
        .build();
    willSaveArticleEntity = ArticleEntity.builder()
        .id(3L)
        .title("new title")
        .content("new content")
        .build();

    final String baseLink = ArticleConst.CONTEXT_PATH + ArticleConst.CONTEXT_ARTICLE + "/";

    savedArticleEntityLink1 = baseLink + savedArticleEntity1.getId().toString();
    savedArticleEntityLink2 = baseLink + savedArticleEntity2.getId().toString();
    willUpdateArticleEntityLink = baseLink + willUpdateArticleEntity.getId().toString();
    willSaveArticleEntityLink = baseLink + willSaveArticleEntity.getId().toString();

    articlesService = new ArticlesServiceImpl(articlesRepository);
  }

  @Test
  public void whenFindAllArticles_thenReturnAllArticles() {
    final List<ArticleEntity> savedArticleEntityList = new ArrayList<>();
    savedArticleEntityList.add(savedArticleEntity1);
    savedArticleEntityList.add(savedArticleEntity2);

    when(articlesRepository.findAll()).thenReturn(savedArticleEntityList);

    final List<Article> articles = articlesService.findAllArticles();

    assertThat(articles.size()).isEqualTo(savedArticleEntityList.size());

    Article article = articles.get(0);
    assertThat(article.getType()).isEqualTo(ArticleConst.TYPE_ARTICLES);
    assertThat(article.getId()).isEqualTo(savedArticleEntity1.getId().toString());
    assertThat(article.getAttributes().getTitle()).isEqualTo(savedArticleEntity1.getTitle());
    assertThat(article.getAttributes().getContent()).isEqualTo(savedArticleEntity1.getContent());
    assertThat(article.getLinks().getSelf()).isEqualTo(savedArticleEntityLink1);

    article = articles.get(1);
    assertThat(article.getType()).isEqualTo(ArticleConst.TYPE_ARTICLES);
    assertThat(article.getId()).isEqualTo(savedArticleEntity2.getId().toString());
    assertThat(article.getAttributes().getTitle()).isEqualTo(savedArticleEntity2.getTitle());
    assertThat(article.getAttributes().getContent()).isEqualTo(savedArticleEntity2.getContent());
    assertThat(article.getLinks().getSelf()).isEqualTo(savedArticleEntityLink2);
  }

  @Test
  public void whenFindByArticleId_thenReturnArticle() {
    when(articlesRepository.findById(savedArticleEntity1.getId())).thenReturn(Optional.of(savedArticleEntity1));

    final Article article = articlesService.findArticleById(savedArticleEntity1.getId().toString());

    assertThat(article.getType()).isEqualTo(ArticleConst.TYPE_ARTICLES);
    assertThat(article.getId()).isEqualTo(savedArticleEntity1.getId().toString());
    assertThat(article.getAttributes().getTitle()).isEqualTo(savedArticleEntity1.getTitle());
    assertThat(article.getAttributes().getContent()).isEqualTo(savedArticleEntity1.getContent());
    assertThat(article.getLinks().getSelf()).isEqualTo(savedArticleEntityLink1);
  }

  @Test(expected = ArticleNotFoundException.class)
  public void whenFindByWrongArticleId_thenThrowException() {
    final String  wrongArticleId = "3";

    when(articlesRepository.findById(Long.valueOf(wrongArticleId))).thenReturn(Optional.empty());

    articlesService.findArticleById(wrongArticleId);
  }

  @Test
  public void whenNewArticle_thenSaveArticle() {
    final Article newArticle = Article.builder()
    .type(ArticleConst.TYPE_ARTICLES)
    .attributes(new Attributes(willSaveArticleEntity.getTitle(), willSaveArticleEntity.getContent()))
    .build();

    when(articlesRepository.save(any(ArticleEntity.class))).thenReturn(willSaveArticleEntity);

    final Article article = articlesService.saveArticle(newArticle);

    assertThat(article.getType()).isEqualTo(ArticleConst.TYPE_ARTICLES);
    assertThat(article.getId()).isEqualTo(willSaveArticleEntity.getId().toString());
    assertThat(article.getAttributes().getTitle()).isEqualTo(willSaveArticleEntity.getTitle());
    assertThat(article.getAttributes().getContent()).isEqualTo(willSaveArticleEntity.getContent());
    assertThat(article.getLinks().getSelf()).isEqualTo(willSaveArticleEntityLink);
  }

  @Test
  public void whenUpdateArticle_thenUpdateArticle() {
    final Article updateArticle = Article.builder()
        .type(ArticleConst.TYPE_ARTICLES)
        .attributes(new Attributes(willUpdateArticleEntity.getTitle(), willUpdateArticleEntity.getContent()))
        .build();

    when(articlesRepository.findById(willUpdateArticleEntity.getId())).thenReturn(Optional.of(willUpdateArticleEntity));

    final Article article = articlesService.updateArticle(willUpdateArticleEntity.getId().toString(), updateArticle);

    assertThat(article.getType()).isEqualTo(ArticleConst.TYPE_ARTICLES);
    assertThat(article.getId()).isEqualTo(willUpdateArticleEntity.getId().toString());
    assertThat(article.getAttributes().getTitle()).isEqualTo(willUpdateArticleEntity.getTitle());
    assertThat(article.getAttributes().getContent()).isEqualTo(willUpdateArticleEntity.getContent());
    assertThat(article.getLinks().getSelf()).isEqualTo(willUpdateArticleEntityLink);
  }

  @Test(expected = ArticleNotFoundException.class)
  public void whenUpdateNotExistArticle_thenThrowException() {
    final String  wrongArticleId = "3";
    final Article updateArticle = Article.builder()
        .type(ArticleConst.TYPE_ARTICLES)
        .attributes(new Attributes(willUpdateArticleEntity.getTitle(), willUpdateArticleEntity.getContent()))
        .build();

    when(articlesRepository.findById(Long.valueOf(wrongArticleId))).thenReturn(Optional.empty());

    articlesService.updateArticle(wrongArticleId, updateArticle);
  }

  @Test
  public void whenDeleteArticle_thenDeleteArticle() {
    final List<ArticleEntity> savedArticleEntityList = new ArrayList<>();
    savedArticleEntityList.add(savedArticleEntity1);
    savedArticleEntityList.add(savedArticleEntity2);

    when(articlesRepository.findAll()).thenReturn(savedArticleEntityList);

    List<Article> articles = articlesService.findAllArticles();

    assertThat(articles.size()).isEqualTo(savedArticleEntityList.size());

    articlesService.deleteArticle(savedArticleEntity1.getId().toString());
    savedArticleEntityList.remove(0);
    articles = articlesService.findAllArticles();

    assertThat(articles.size()).isEqualTo(savedArticleEntityList.size());

    Article article = articles.get(0);
    assertThat(article.getType()).isEqualTo(ArticleConst.TYPE_ARTICLES);
    assertThat(article.getId()).isEqualTo(savedArticleEntity2.getId().toString());
    assertThat(article.getAttributes().getTitle()).isEqualTo(savedArticleEntity2.getTitle());
    assertThat(article.getAttributes().getContent()).isEqualTo(savedArticleEntity2.getContent());
    assertThat(article.getLinks().getSelf()).isEqualTo(savedArticleEntityLink2);
  }
}
