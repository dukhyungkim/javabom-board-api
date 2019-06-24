package com.gentooboy.javabom.boardapi.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gentooboy.javabom.boardapi.constant.ArticleConst;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.articles.Attributes;
import com.gentooboy.javabom.boardapi.model.response.ArticleData;
import com.gentooboy.javabom.boardapi.service.ArticlesService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ArticleControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ArticlesService articlesService;

  @Autowired
  ObjectMapper objectMapper;

  private static final String BASE_URL = ArticleConst.CONTEXT_ARTICLE;
  private static final String SOURCE_POINTER = "/data/type/articles/";
  private static final String ENCODING = "utf-8";
  private static final int SINGLE_ARTICLE_DATA_SIZE = 4;

  private Article article1;
  private Article article2;

  @Before
  public void setUp() {
    article1 = Article.builder()
        .type(ArticleConst.TYPE_ARTICLES)
        .attributes(new Attributes("Initial Article", "This is content of article"))
        .build();

    article2 = Article.builder()
        .type(ArticleConst.TYPE_ARTICLES)
        .attributes(new Attributes("New Article", "This is content of new article"))
        .build();
  }

  @Test
  public void whenNoArticles_ReturnNoData() throws Exception {
    final List<Article> articleList = Collections.emptyList();

    final ResultActions actions = mockMvc.perform(get(BASE_URL)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isNoContent())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(articleList.size()));
  }

  @Test
  public void whenRequestAllArticles_ReturnAllArticles() throws Exception {
    final List<Article> articleList = new ArrayList<>();
    articleList.add(articlesService.saveArticle(article1));
    articleList.add(articlesService.saveArticle(article2));

    final ResultActions actions = mockMvc.perform(get(BASE_URL)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(articleList.size()))
        .andExpect(jsonPath("$.data[0].type").value(ArticleConst.TYPE_ARTICLES))
        .andExpect(jsonPath("$.data[0].id").value(articleList.get(0).getId()))
        .andExpect(jsonPath("$.data[0].attributes.title").value(articleList.get(0).getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[0].attributes.content").value(articleList.get(0).getAttributes().getContent()))
        .andExpect(jsonPath("$.data[0].links.self").value(articleList.get(0).getLinks().getSelf()))
        .andExpect(jsonPath("$.data[1].type").value(ArticleConst.TYPE_ARTICLES))
        .andExpect(jsonPath("$.data[1].id").value(articleList.get(1).getId()))
        .andExpect(jsonPath("$.data[1].attributes.title").value(articleList.get(1).getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[1].attributes.content").value(articleList.get(1).getAttributes().getContent()))
        .andExpect(jsonPath("$.data[1].links.self").value(articleList.get(1).getLinks().getSelf()));
  }

  @Test
  public void whenRequestArticleById_ReturnArticle() throws Exception {
    final List<Article> articleList = new ArrayList<>();
    articleList.add(articlesService.saveArticle(article1));
    articleList.add(articlesService.saveArticle(article2));

    final String url = BASE_URL + "/" + articleList.get(1).getId();
    final ResultActions actions = mockMvc.perform(get(url)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(SINGLE_ARTICLE_DATA_SIZE))
        .andExpect(jsonPath("$.data.type").value(ArticleConst.TYPE_ARTICLES))
        .andExpect(jsonPath("$.data.id").value(articleList.get(1).getId()))
        .andExpect(jsonPath("$.data.attributes.title").value(articleList.get(1).getAttributes().getTitle()))
        .andExpect(jsonPath("$.data.attributes.content").value(articleList.get(1).getAttributes().getContent()))
        .andExpect(jsonPath("$.data.links.self").value(articleList.get(1).getLinks().getSelf()));
  }

  @Test
  public void whenRequestArticleByWrongId_ReturnArticleNotFoundError() throws Exception {
    final String wrongArticleId = "1000";
    final String url = BASE_URL + "/" + wrongArticleId;
    final ResultActions actions = mockMvc.perform(get(url)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.error.size()").value(SINGLE_ARTICLE_DATA_SIZE))
        .andExpect(jsonPath("$.error.status").value(HttpStatus.NOT_FOUND.value()))
        .andExpect(jsonPath("$.error.source.pointer").value(SOURCE_POINTER + wrongArticleId))
        .andExpect(jsonPath("$.error.title").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
        .andExpect(jsonPath("$.error.message").value(ArticleConst.MESSAGE_NOT_FOUND));
  }

  @Test
  public void whenPostNewArticle_thenSaveArticle() throws Exception {
    final Article newArticle = Article.builder()
        .type(article1.getType())
        .attributes(
            new Attributes(article1.getAttributes().getTitle(),
                article1.getAttributes().getContent()))
        .build();
    final ArticleData newArticleData = new ArticleData<>(newArticle);

    final ResultActions actions = mockMvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(newArticleData)));

    actions
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(SINGLE_ARTICLE_DATA_SIZE))
        .andExpect(jsonPath("$.data.type").value(ArticleConst.TYPE_ARTICLES))
        .andExpect(jsonPath("$.data.id").isString())
        .andExpect(jsonPath("$.data.attributes.title").value(newArticle.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data.attributes.content").value(newArticle.getAttributes().getContent()))
        .andExpect(jsonPath("$.data.links.self").isString());
  }

  @Test
  public void whenUpdateArticle_thenReturnSuccess() throws Exception {
    final String updateTitle = "update title";
    final String updateContent = "update content";

    final List<Article> articleList = new ArrayList<>();
    articleList.add(articlesService.saveArticle(article1));
    articleList.add(articlesService.saveArticle(article2));

    final Article updateArticle = Article.builder()
        .type(article1.getType())
        .attributes(new Attributes(updateTitle, updateContent))
        .build();

    final ArticleData articleData = new ArticleData<>(updateArticle);

    final String url = BASE_URL + "/" + articleList.get(0).getId();
    final ResultActions actions = mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(articleData)));

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(SINGLE_ARTICLE_DATA_SIZE))
        .andExpect(jsonPath("$.data.type").value(ArticleConst.TYPE_ARTICLES))
        .andExpect(jsonPath("$.data.id").value(articleList.get(0).getId()))
        .andExpect(jsonPath("$.data.attributes.title").value(updateTitle))
        .andExpect(jsonPath("$.data.attributes.content").value(updateContent))
        .andExpect(jsonPath("$.data.links.self").value(articleList.get(0).getLinks().getSelf()));

    final ResultActions checkActions = mockMvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(articleData)));

    checkActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(SINGLE_ARTICLE_DATA_SIZE))
        .andExpect(jsonPath("$.data.type").value(ArticleConst.TYPE_ARTICLES))
        .andExpect(jsonPath("$.data.id").value(articleList.get(0).getId()))
        .andExpect(jsonPath("$.data.attributes.title").value(updateTitle))
        .andExpect(jsonPath("$.data.attributes.content").value(updateContent))
        .andExpect(jsonPath("$.data.links.self").value(articleList.get(0).getLinks().getSelf()));
  }

  @Test
  public void whenUpdateArticleWithTitle_thenReturnSuccess() throws Exception {
    final String updateTitle = "update title";

    final List<Article> articleList = new ArrayList<>();
    articleList.add(articlesService.saveArticle(article1));
    articleList.add(articlesService.saveArticle(article2));

    final Attributes attributes = Attributes.builder()
        .title(updateTitle)
        .build();

    final Article updateArticle = Article.builder()
        .type(article1.getType())
        .attributes(attributes)
        .build();

    final ArticleData articleData = new ArticleData<>(updateArticle);

    final String url = BASE_URL + "/" + articleList.get(0).getId();
    final ResultActions actions = mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(articleData)));

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(SINGLE_ARTICLE_DATA_SIZE))
        .andExpect(jsonPath("$.data.type").value(ArticleConst.TYPE_ARTICLES))
        .andExpect(jsonPath("$.data.id").value(articleList.get(0).getId()))
        .andExpect(jsonPath("$.data.attributes.title").value(updateTitle))
        .andExpect(jsonPath("$.data.attributes.content").value(article1.getAttributes().getContent()))
        .andExpect(jsonPath("$.data.links.self").value(articleList.get(0).getLinks().getSelf()));
  }

  @Test
  public void whenUpdateArticleWithContent_thenReturnSuccess() throws Exception {
    final String updateContent = "update content";

    final List<Article> articleList = new ArrayList<>();
    articleList.add(articlesService.saveArticle(article1));
    articleList.add(articlesService.saveArticle(article2));

    final Attributes attributes = Attributes.builder()
        .content(updateContent)
        .build();

    final Article updateArticle = Article.builder()
        .type(article1.getType())
        .attributes(attributes)
        .build();

    final ArticleData articleData = new ArticleData<>(updateArticle);

    final String url = BASE_URL + "/" + articleList.get(0).getId();
    final ResultActions updateActions = mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(articleData)));

    updateActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(SINGLE_ARTICLE_DATA_SIZE))
        .andExpect(jsonPath("$.data.type").value(ArticleConst.TYPE_ARTICLES))
        .andExpect(jsonPath("$.data.id").value(articleList.get(0).getId()))
        .andExpect(jsonPath("$.data.attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data.attributes.content").value(updateContent))
        .andExpect(jsonPath("$.data.links.self").value(articleList.get(0).getLinks().getSelf()));

    final ResultActions checkActions = mockMvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(articleData)));

    checkActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(SINGLE_ARTICLE_DATA_SIZE))
        .andExpect(jsonPath("$.data.type").value(ArticleConst.TYPE_ARTICLES))
        .andExpect(jsonPath("$.data.id").value(articleList.get(0).getId()))
        .andExpect(jsonPath("$.data.attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data.attributes.content").value(updateContent))
        .andExpect(jsonPath("$.data.links.self").value(articleList.get(0).getLinks().getSelf()));
  }

  @Test
  public void whenUpdateArticle_thenReturnNotFound() throws Exception {
    final String wrongArticleId = "1000";
    final ArticleData articleData = new ArticleData<>(article1);

    final String url = BASE_URL + "/" + wrongArticleId;
    final ResultActions actions = mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(articleData)));

    actions
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.error.size()").value(4))
        .andExpect(jsonPath("$.error.status").value(String.valueOf(HttpStatus.NOT_FOUND.value())))
        .andExpect(jsonPath("$.error.source.pointer").value(SOURCE_POINTER + wrongArticleId))
        .andExpect(jsonPath("$.error.title").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
        .andExpect(jsonPath("$.error.message").value(ArticleConst.MESSAGE_NOT_FOUND));
  }

  @Test
  public void whenDeleteArticle_thenReturnSuccess() throws Exception {
    final List<Article> articleList = new ArrayList<>();
    articleList.add(articlesService.saveArticle(article1));
    articleList.add(articlesService.saveArticle(article2));

    final String url = BASE_URL + "/" + articleList.get(0).getId();
    final ResultActions deleteActions = mockMvc.perform(delete(url)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING));
    articleList.remove(0);

    deleteActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());

    final ResultActions getActions = mockMvc.perform(get(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING));

    getActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.size()").value(articleList.size()))
        .andExpect(jsonPath("$.data[0].type").value(ArticleConst.TYPE_ARTICLES))
        .andExpect(jsonPath("$.data[0].id").value(articleList.get(0).getId()))
        .andExpect(jsonPath("$.data[0].attributes.title").value(articleList.get(0).getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[0].attributes.content").value(articleList.get(0).getAttributes().getContent()))
        .andExpect(jsonPath("$.data[0].links.self").value(articleList.get(0).getLinks().getSelf()));
  }
}
