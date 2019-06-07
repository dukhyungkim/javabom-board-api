package com.gentooboy.javabom.boardapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.exception.ArticleSaveErrorException;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.articles.Attributes;
import com.gentooboy.javabom.boardapi.model.articles.Links;
import com.gentooboy.javabom.boardapi.model.error.Errors;
import com.gentooboy.javabom.boardapi.model.error.Source;
import com.gentooboy.javabom.boardapi.model.request.NewArticle;
import com.gentooboy.javabom.boardapi.model.request.NewArticleData;
import com.gentooboy.javabom.boardapi.service.ArticlesService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(ArticlesController.class)
public class ArticlesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ArticlesService service;

  @Autowired
  ObjectMapper objectMapper;

  private static final String BASE_URL = "/articles";
  private static final String ENCODING = "utf-8";

  private Article article1;
  private Article article2;

  @Before
  public void setup() {
    article1 = Article.builder()
        .type("articles")
        .id("1")
        .attributes(new Attributes("Initial Article", "This is content of article"))
        .links(new Links("https://board-api/api/v1/articles/1"))
        .build();
    article2 = Article.builder()
        .type("articles")
        .id("2")
        .attributes(new Attributes("New Article", "This is content of new article"))
        .links(new Links("https://board-api/api/v1/articles/2"))
        .build();
  }

  @Test
  public void getArticleListReturnSuccessSingleData() throws Exception {
    List<Article> articleList = new ArrayList<>();
    articleList.add(article1);

    when(service.findAllArticles())
        .thenReturn(articleList);

    final ResultActions actions = mockMvc.perform(get(BASE_URL)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(1))
        .andExpect(jsonPath("$.data[0].type").value(article1.getType()))
        .andExpect(jsonPath("$.data[0].id").value(article1.getId()))
        .andExpect(jsonPath("$.data[0].attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[0].attributes.body").value(article1.getAttributes().getBody()))
        .andExpect(jsonPath("$.data[0].links.self").value(article1.getLinks().getSelf()));
  }

  @Test
  public void getArticleListReturnSuccessMultipleData() throws Exception {
    List<Article> articleList = new ArrayList<>();
    articleList.add(article1);
    articleList.add(article2);

    when(service.findAllArticles())
        .thenReturn(articleList);

    final ResultActions actions = mockMvc.perform(get(BASE_URL)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(2))
        .andExpect(jsonPath("$.data[0].type").value(article1.getType()))
        .andExpect(jsonPath("$.data[0].id").value(article1.getId()))
        .andExpect(jsonPath("$.data[0].attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[0].attributes.body").value(article1.getAttributes().getBody()))
        .andExpect(jsonPath("$.data[0].links.self").value(article1.getLinks().getSelf()))
        .andExpect(jsonPath("$.data[1].type").value(article2.getType()))
        .andExpect(jsonPath("$.data[1].id").value(article2.getId()))
        .andExpect(jsonPath("$.data[1].attributes.title").value(article2.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[1].attributes.body").value(article2.getAttributes().getBody()))
        .andExpect(jsonPath("$.data[1].links.self").value(article2.getLinks().getSelf()));
  }

  @Test
  public void getArticleListReturnNoContent() throws Exception {
    List<Article> articleList = Collections.emptyList();

    when(service.findAllArticles())
        .thenReturn(articleList);

    final ResultActions actions = mockMvc.perform(get(BASE_URL)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isNoContent())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(0));
  }

  @Test
  public void getArticleReturnSuccess() throws Exception {
    when(service.findArticleById(1L))
        .thenReturn(article1);

    final String url = BASE_URL + "/1";
    final ResultActions actions = mockMvc.perform(get(url)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(4))
        .andExpect(jsonPath("$.data.type").value(article1.getType()))
        .andExpect(jsonPath("$.data.id").value(article1.getId()))
        .andExpect(jsonPath("$.data.attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data.attributes.body").value(article1.getAttributes().getBody()))
        .andExpect(jsonPath("$.data.links.self").value(article1.getLinks().getSelf()));
  }

  @Test
  public void getArticleReturnErrorNotFound() throws Exception {
    Errors errors = Errors.builder()
        .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
        .source(new Source("/data/type/articles/0"))
        .title(HttpStatus.NOT_FOUND.getReasonPhrase())
        .detail("The article id 0 is not found.")
        .build();

    when(service.findArticleById(0L))
        .thenThrow(new ArticleNotFoundException(errors.getStatus(), errors.getTitle(),
            errors.getDetail()));

    final String url = BASE_URL + "/0";
    final ResultActions actions = mockMvc.perform(get(url)
        .characterEncoding(ENCODING));
    actions
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.errors.size()").value(4))
        .andExpect(jsonPath("$.errors.status").value(errors.getStatus()))
        .andExpect(jsonPath("$.errors.source.pointer").value(errors.getSource().getPointer()))
        .andExpect(jsonPath("$.errors.title").value(errors.getTitle()))
        .andExpect(jsonPath("$.errors.detail").value(errors.getDetail()));
  }

  @Test
  public void newArticleReturnSuccess() throws Exception {
    NewArticle newArticle = NewArticle.builder()
        .type(article1.getType())
        .attributes(
            new Attributes(article1.getAttributes().getTitle(), article1.getAttributes().getBody()))
        .build();
    NewArticleData newArticleData = new NewArticleData(newArticle);

    when(service.saveArticle(any(NewArticle.class))).thenReturn(article1);

    final ResultActions actions = mockMvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(newArticleData)));

    actions
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(4))
        .andExpect(jsonPath("$.data.type").value(article1.getType()))
        .andExpect(jsonPath("$.data.id").value(article1.getId()))
        .andExpect(jsonPath("$.data.attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data.attributes.body").value(article1.getAttributes().getBody()))
        .andExpect(jsonPath("$.data.links.self").value(article1.getLinks().getSelf()));
  }

  @Test
  public void newArticleReturnInternalServerError() throws Exception {
    NewArticle newArticle = NewArticle.builder()
        .type(article1.getType())
        .attributes(
            new Attributes(article1.getAttributes().getTitle(), article1.getAttributes().getBody()))
        .build();
    NewArticleData newArticleData = new NewArticleData(newArticle);

    Errors errors = Errors.builder()
        .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
        .source(new Source("/data/type/articles"))
        .title(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .detail("Can't save the article to database.")
        .build();

    when(service.saveArticle(any(NewArticle.class))).thenThrow(new ArticleSaveErrorException(errors.getStatus(), errors.getTitle(), errors.getDetail()));

    final ResultActions actions = mockMvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(newArticleData)));

    actions
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.errors.size()").value(4))
        .andExpect(jsonPath("$.errors.status").value(errors.getStatus()))
        .andExpect(jsonPath("$.errors.source.pointer").value(errors.getSource().getPointer()))
        .andExpect(jsonPath("$.errors.title").value(errors.getTitle()))
        .andExpect(jsonPath("$.errors.detail").value(errors.getDetail()));
  }
}