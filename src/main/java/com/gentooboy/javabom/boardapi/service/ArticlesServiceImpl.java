package com.gentooboy.javabom.boardapi.service;

import com.gentooboy.javabom.boardapi.constant.ArticleConst;
import com.gentooboy.javabom.boardapi.entity.ArticleEntity;
import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.articles.Attributes;
import com.gentooboy.javabom.boardapi.model.articles.Links;
import com.gentooboy.javabom.boardapi.repository.ArticlesRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ArticlesServiceImpl implements ArticlesService {

  private final ArticlesRepository articlesRepository;

  public ArticlesServiceImpl(ArticlesRepository articlesRepository) {
    this.articlesRepository = articlesRepository;
  }

  @Override
  public List<Article> findAllArticles() {
    List<ArticleEntity> articleEntityList = articlesRepository.findAll();

    List<Article> articleList = new ArrayList<>();

    for (ArticleEntity articleEntity : articleEntityList) {
      Article article = ConvertEntityToArticle(articleEntity);
      articleList.add(article);
    }

    return articleList;
  }

  @Override
  public Article findArticleById(String articleId) throws ArticleNotFoundException {
    Optional<ArticleEntity> found = articlesRepository.findById(ConvertArticleId(articleId));

    ArticleNotFoundException exception = new ArticleNotFoundException(
        ArticleConst.MESSAGE_NOT_FOUND);
    ArticleEntity articleEntity = found.orElseThrow(() -> exception);

    return ConvertEntityToArticle(articleEntity);
  }

  @Override
  public Article saveArticle(final Article newArticle) {
    ArticleEntity articleEntity = articlesRepository.save(ConvertArticleToEntity(newArticle));

    return ConvertEntityToArticle(articleEntity);
  }

  @Override
  public Article updateArticle(final String articleId, Article article)
      throws ArticleNotFoundException {
    if (!articlesRepository.existsById(ConvertArticleId(articleId))) {
      throw new ArticleNotFoundException(ArticleConst.MESSAGE_NOT_FOUND);
    }

    ArticleEntity articleEntity = articlesRepository.save(ConvertArticleToEntity(article));

    return ConvertEntityToArticle(articleEntity);
  }

  @Override
  public void deleteArticle(final String articleId) {
    articlesRepository.deleteById(ConvertArticleId(articleId));
  }

  private Article ConvertEntityToArticle(final ArticleEntity articleEntity) {
    final String LINK_BASE = ArticleConst.CONTEXT_PATH + ArticleConst.CONTEXT_ARTICLE + "/";

    return Article.builder()
        .type(ArticleConst.TYPE_ARTICLES)
        .id(ConvertArticleId(articleEntity.getId()))
        .attributes(new Attributes(articleEntity.getTitle(), articleEntity.getContent()))
        .links(new Links(LINK_BASE + articleEntity.getId().toString()))
        .build();
  }

  private ArticleEntity ConvertArticleToEntity(final Article article) {
    return ArticleEntity.builder()
        .title(article.getAttributes().getTitle())
        .content(article.getAttributes().getContent())
        .build();
  }

  private Long ConvertArticleId(String articleId) {
    return Long.valueOf(articleId);
  }

  private String ConvertArticleId(Long articleId) {
    return articleId.toString();
  }
}
