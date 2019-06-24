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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticlesServiceImpl implements ArticlesService {

  private final ArticlesRepository articlesRepository;

  @Override
  public List<Article> findAllArticles() {
    final List<ArticleEntity> articleEntityList = articlesRepository.findAll();

    final List<Article> articleList = new ArrayList<>();

    for (ArticleEntity articleEntity : articleEntityList) {
      final Article article = ConvertEntityToArticle(articleEntity);
      articleList.add(article);
    }

    return articleList;
  }

  @Override
  public Article findArticleById(final String articleId) throws ArticleNotFoundException {
    final Optional<ArticleEntity> found = articlesRepository.findById(ConvertArticleId(articleId));

    final ArticleEntity articleEntity = found.orElseThrow(() -> new ArticleNotFoundException(
        ArticleConst.MESSAGE_NOT_FOUND));

    return ConvertEntityToArticle(articleEntity);
  }

  @Override
  public Article saveArticle(final Article newArticle) {
    ArticleEntity articleEntity = articlesRepository.save(ConvertArticleToEntity(newArticle));

    return ConvertEntityToArticle(articleEntity);
  }

  @Override
  public Article updateArticle(final String articleId, final Article article)
      throws ArticleNotFoundException {
    final Optional<ArticleEntity> found = articlesRepository.findById(ConvertArticleId(articleId));

    final ArticleEntity articleEntity = found.orElseThrow(() -> new ArticleNotFoundException(
        ArticleConst.MESSAGE_NOT_FOUND));

    final String title = article.getAttributes().getTitle();
    if (title != null && title.trim().length() != 0) {
      articleEntity.setTitle(title);
    }

    final String content = article.getAttributes().getContent();
    if (content != null) {
      articleEntity.setContent(content);
    }

    return ConvertEntityToArticle(articlesRepository.save(articleEntity));
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

  private Long ConvertArticleId(final String articleId) {
    return Long.valueOf(articleId);
  }

  private String ConvertArticleId(final Long articleId) {
    return articleId.toString();
  }
}
