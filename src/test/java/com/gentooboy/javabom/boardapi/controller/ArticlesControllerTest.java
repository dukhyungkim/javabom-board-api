package com.gentooboy.javabom.boardapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gentooboy.javabom.boardapi.constant.ArticleConst;
import com.gentooboy.javabom.boardapi.exception.ArticleNotFoundException;
import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.articles.Attributes;
import com.gentooboy.javabom.boardapi.model.articles.Links;
import com.gentooboy.javabom.boardapi.model.error.Error;
import com.gentooboy.javabom.boardapi.model.error.Source;
import com.gentooboy.javabom.boardapi.model.response.ArticleData;
import com.gentooboy.javabom.boardapi.service.ArticlesService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

  private static final String BASE_URL = ArticleConst.CONTEXT_ARTICLE;
  private static final String ENCODING = "utf-8";

  private final Article article1;
  private final Article article2;

  public ArticlesControllerTest() {
    String articleId = "1";
    article1 = Article.builder()
        .type(ArticleConst.TYPE_ARTICLES)
        .id(articleId)
        .attributes(new Attributes("Initial Article", "This is content of article"))
        .links(new Links(ArticleConst.CONTEXT_PATH + BASE_URL + "/" + articleId))
        .build();

    articleId = "2";
    article2 = Article.builder()
        .type(ArticleConst.TYPE_ARTICLES)
        .id(articleId)
        .attributes(new Attributes("New Article", "This is content of new article"))
        .links(new Links(ArticleConst.CONTEXT_PATH + BASE_URL + "/" + articleId))
        .build();
  }

  @Test
  public void whenGetArticleList_thenReturnSuccessSingleData() throws Exception {
    List<Article> articleList = new ArrayList<>();
    articleList.add(article1);

    when(service.findAllArticles())
        .thenReturn(articleList);

    final ResultActions actions = mockMvc.perform(get(BASE_URL)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(articleList.size()))
        .andExpect(jsonPath("$.data[0].type").value(article1.getType()))
        .andExpect(jsonPath("$.data[0].id").value(article1.getId()))
        .andExpect(jsonPath("$.data[0].attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[0].attributes.content").value(article1.getAttributes().getContent()))
        .andExpect(jsonPath("$.data[0].links.self").value(article1.getLinks().getSelf()));
  }

  @Test
  public void whenGetArticleList_thenReturnSuccessMultipleData() throws Exception {
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
        .andExpect(jsonPath("$.data.size()").value(articleList.size()))
        .andExpect(jsonPath("$.data[0].type").value(article1.getType()))
        .andExpect(jsonPath("$.data[0].id").value(article1.getId()))
        .andExpect(jsonPath("$.data[0].attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[0].attributes.content").value(article1.getAttributes().getContent()))
        .andExpect(jsonPath("$.data[0].links.self").value(article1.getLinks().getSelf()))
        .andExpect(jsonPath("$.data[1].type").value(article2.getType()))
        .andExpect(jsonPath("$.data[1].id").value(article2.getId()))
        .andExpect(jsonPath("$.data[1].attributes.title").value(article2.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[1].attributes.content").value(article2.getAttributes().getContent()))
        .andExpect(jsonPath("$.data[1].links.self").value(article2.getLinks().getSelf()));
  }

  @Test
  public void whenGetArticleList_thenReturnNoContent() throws Exception {
    final List<Article> articleList = Collections.emptyList();

    when(service.findAllArticles())
        .thenReturn(articleList);

    final ResultActions actions = mockMvc.perform(get(BASE_URL)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isNoContent())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(articleList.size()));
  }

  @Test
  public void whenGetArticle_thenReturnSuccess() throws Exception {
    when(service.findArticleById(article1.getId())).thenReturn(article1);

    final String url = BASE_URL + "/" + article1.getId();
    final ResultActions actions = mockMvc.perform(get(url)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(4))
        .andExpect(jsonPath("$.data.type").value(article1.getType()))
        .andExpect(jsonPath("$.data.id").value(article1.getId()))
        .andExpect(jsonPath("$.data.attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data.attributes.content").value(article1.getAttributes().getContent()))
        .andExpect(jsonPath("$.data.links.self").value(article1.getLinks().getSelf()));
  }

  @Test
  public void whenGetArticle_thenReturnErrorNotFound() throws Exception {
    final String articleId = "0";
    final Error error = Error.builder()
        .source(new Source("/data/type/articles/" + articleId))
        .message(ArticleConst.MESSAGE_NOT_FOUND)
        .build();

    when(service.findArticleById(eq("0")))
        .thenThrow(new ArticleNotFoundException(error.getMessage()));

    final String url = BASE_URL + "/" + articleId;
    final ResultActions actions = mockMvc.perform(get(url)
        .characterEncoding(ENCODING));
    actions
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.error.size()").value(4))
        .andExpect(jsonPath("$.error.status").value(String.valueOf(HttpStatus.NOT_FOUND.value())))
        .andExpect(jsonPath("$.error.source.pointer").value(error.getSource().getPointer()))
        .andExpect(jsonPath("$.error.title").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
        .andExpect(jsonPath("$.error.message").value(error.getMessage()));
  }

  @Test
  public void whenNewArticle_thenReturnSuccess() throws Exception {
    final Article newArticle = Article.builder()
        .type(article1.getType())
        .attributes(
            new Attributes(article1.getAttributes().getTitle(),
                article1.getAttributes().getContent()))
        .build();
    final ArticleData newArticleData = new ArticleData<>(newArticle);

    when(service.saveArticle(any(Article.class))).thenReturn(article1);

    final ResultActions actions = mockMvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(newArticleData)))
        .andDo(print());

    actions
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(4))
        .andExpect(jsonPath("$.data.type").value(article1.getType()))
        .andExpect(jsonPath("$.data.id").value(article1.getId()))
        .andExpect(jsonPath("$.data.attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data.attributes.content").value(article1.getAttributes().getContent()))
        .andExpect(jsonPath("$.data.links.self").value(article1.getLinks().getSelf()));
  }

  @Test
  public void whenUpdateArticle_thenReturnSuccess() throws Exception {
    final ArticleData articleData = new ArticleData<>(article1);

    when(service.updateArticle(eq(article1.getId()), any(Article.class))).thenReturn(article1);

    final String url = BASE_URL + "/" + article1.getId();
    final ResultActions actions = mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(articleData)));

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(4))
        .andExpect(jsonPath("$.data.type").value(article1.getType()))
        .andExpect(jsonPath("$.data.id").value(article1.getId()))
        .andExpect(jsonPath("$.data.attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data.attributes.content").value(article1.getAttributes().getContent()))
        .andExpect(jsonPath("$.data.links.self").value(article1.getLinks().getSelf()));
  }

  @Test
  public void whenUpdateArticle_thenReturnNotFound() throws Exception {
    final Error error = Error.builder()
        .source(new Source("/data/type/articles/" + article1.getId()))
        .message(ArticleConst.MESSAGE_NOT_FOUND)
        .build();

    final ArticleData articleData = new ArticleData<>(article1);

    final ArticleNotFoundException exception = new ArticleNotFoundException(error.getMessage());

    when(service.updateArticle(eq(article1.getId()), any(Article.class))).thenThrow(exception);

    final String url = BASE_URL + "/" + article1.getId();
    final ResultActions actions = mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING)
        .content(objectMapper.writeValueAsString(articleData)));

    actions
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.error.size()").value(4))
        .andExpect(jsonPath("$.error.status").value(String.valueOf(HttpStatus.NOT_FOUND.value())))
        .andExpect(jsonPath("$.error.source.pointer").value(error.getSource().getPointer()))
        .andExpect(jsonPath("$.error.title").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
        .andExpect(jsonPath("$.error.message").value(error.getMessage()));
  }

  @Test
  public void whenDeleteArticle_thenReturnSuccess() throws Exception {
    final String url = BASE_URL + "/" + article1.getId();
    final ResultActions actions = mockMvc.perform(delete(url)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(ENCODING));

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$.data").value(""));
  }
}